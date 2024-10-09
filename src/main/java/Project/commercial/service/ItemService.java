package Project.commercial.service;

import Project.commercial.domain.Category;
import Project.commercial.domain.Color;
import Project.commercial.domain.DetailCategory;
import Project.commercial.domain.DetailImage;
import Project.commercial.domain.Item;
import Project.commercial.domain.Size;
import Project.commercial.domain.ThumbnailImage;
import Project.commercial.dto.item.CreateItemDto;
import Project.commercial.dto.item.DetailImageDto;
import Project.commercial.dto.item.ItemDto;
import Project.commercial.dto.item.ItemSearchConditionDto;
import Project.commercial.dto.item.ItemSearchResponseDto;
import Project.commercial.dto.item.ResponseItemDto;
import Project.commercial.dto.item.ResponseThumbnailImageDto;
import Project.commercial.dto.item.ResponseUpdateItemDto;
import Project.commercial.dto.item.UpdateItemDto;
import Project.commercial.repository.CategoryRepository;
import Project.commercial.repository.ColorRepository;
import Project.commercial.repository.DetailCategoryRepository;
import Project.commercial.repository.DetailImageRepository;
import Project.commercial.repository.ItemRepository;
import Project.commercial.repository.SizeRepository;
import Project.commercial.repository.ThumbnailImageRepository;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

  private final ItemRepository itemRepository;
  private final DetailImageRepository detailImageRepository;
  private final CategoryRepository categoryRepository;
  private final DetailCategoryRepository detailCategoryRepository;
  private final SizeRepository sizeRepository;
  private final ColorRepository colorRepository;
  private final ThumbnailImageRepository thumbnailImageRepository;
  @Value("$(file.dir)")
  String fileDir;

  @Transactional
  public ResponseItemDto createItem(CreateItemDto createItemDto, MultipartFile thumbnailImage,
      List<MultipartFile> detailImages) throws IOException {
    validateThumbnailImage(thumbnailImage);

    DetailCategory detailCategory = findByIdOrThrow(detailCategoryRepository,
        createItemDto.getDetailCategoryId(), "Invalid Detail Category ID");
    Category category = findByIdOrThrow(categoryRepository, createItemDto.getCategoryId(),
        "Invalid Category ID");
    Color color = findByIdOrThrow(colorRepository, createItemDto.getColorId(), "Invalid Color ID");
    Size size = findByIdOrThrow(sizeRepository, createItemDto.getSizeId(), "Invalid Size ID");

    Item newItem = createNewItem(createItemDto, detailCategory, category, color, size);
    ThumbnailImage thumbnailImageEntity = saveThumbnailImage(thumbnailImage, newItem);

    if (!CollectionUtils.isEmpty(detailImages)) {
      saveDetailImages(detailImages, newItem);
    }

    return ResponseItemDto.builder()
        .id(newItem.getId())
        .category(newItem.getCategory())
        .detailCategory(newItem.getDetailCategory())
        .itemName(newItem.getItemName())
        .description(newItem.getDescription())
        .color(newItem.getColor())
        .size(newItem.getSize())
        .price(comma(newItem.getPrice()))
        .thumbnailImage(thumbnailImageEntity)
        .detailImages(newItem.getDetailImage())
        .build();
  }

  @Transactional(readOnly = true)
  public List<ItemDto> getItems(Pageable pageable) {
    Page<Item> items = itemRepository.findAll(pageable);
    return items.stream()
        .map(item -> ItemDto.builder()
            .id(item.getId())
            .category(item.getCategory())
            .detailCategory(item.getDetailCategory())
            .itemName(item.getItemName())
            .description(item.getDescription())
            .size(item.getSize())
            .color(item.getColor())
            .price(comma(item.getPrice()))
            .thumbnailImage(ResponseThumbnailImageDto.builder().itemId(item.getId())
                .storeImageName(item.getThumbnailImage().getStoreImageName()).build())
            .build())
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public ItemSearchResponseDto getItemsBySearch(ItemSearchConditionDto itemSearchConditionDto,
      Pageable pageable) {
    List<Item> itemList = itemRepository.searchItem(itemSearchConditionDto, pageable);
    Long totalItemCount = itemRepository.getSearchItemCount(itemSearchConditionDto);

    List<ItemDto> itemDtoList = itemList.stream()
        .map(item -> ItemDto.builder()
            .id(item.getId())
            .category(item.getCategory())
            .detailCategory(item.getDetailCategory())
            .itemName(item.getItemName())
            .description(item.getDescription())
            .size(item.getSize())
            .color(item.getColor())
            .price(comma(item.getPrice()))
            .thumbnailImage(ResponseThumbnailImageDto.builder().itemId(item.getId())
                .storeImageName(item.getThumbnailImage().getStoreImageName()).build())
            .build())
        .collect(Collectors.toList());

    return ItemSearchResponseDto.builder()
        .item(itemDtoList)
        .totalCount(totalItemCount)
        .build();
  }

  @Transactional(readOnly = true)
  public ItemDto getItemDetail(Long id) {

    Item item = itemRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품 입니다."));

    List<DetailImageDto> detailImageDtos = Optional.ofNullable(item.getDetailImage())
        .orElse(Collections.emptyList())
        .stream()
        .map(v -> DetailImageDto.builder()
            .id(v.getId())
            .storeImageName(v.getStoreImageName())
            .build())
        .collect(Collectors.toList());

    ResponseThumbnailImageDto thumbnailImageDto = ResponseThumbnailImageDto.builder()
        .itemId(item.getId())
        .storeImageName(item.getThumbnailImage().getStoreImageName())
        .build();

    return ItemDto.builder()
        .id(item.getId())
        .category(item.getCategory())
        .detailCategory(item.getDetailCategory())
        .itemName(item.getItemName())
        .description(item.getDescription())
        .size(item.getSize())
        .color(item.getColor())
        .price(comma(item.getPrice()))
        .thumbnailImage(thumbnailImageDto)
        .detailImages(detailImageDtos)
        .build();

  }

  @Transactional
  public void deleteItem(Long id) {
    Item item = itemRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품 입니다."));

    itemRepository.delete(item);
  }

  @Transactional
  public void updateItem(Long id, UpdateItemDto updateItemDto, MultipartFile newThumbnailImage,
      List<MultipartFile> newDetailImages) throws IOException {
    Item item = itemRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품 입니다."));

    validateThumbnailImage(newThumbnailImage);

    if (thumbnailImageRepository.findByItem_id(item.getId()).isPresent()) {
      thumbnailImageRepository.deleteByItem_id(item.getId());
    }
    ThumbnailImage thumbnailImageEntity = saveThumbnailImage(newThumbnailImage, item);

    List<DetailImage> existingDetailImages = item.getDetailImage();
    if (CollectionUtils.isEmpty(existingDetailImages) && !CollectionUtils.isEmpty(
        newDetailImages)) {
      saveDetailImages(newDetailImages, item);
    } else if (!CollectionUtils.isEmpty(existingDetailImages) && !CollectionUtils.isEmpty(
        newDetailImages)) {
      detailImageRepository.deleteByItemId(item.getId());
      saveDetailImages(newDetailImages, item);
    }

    DetailCategory detailCategory = findByIdOrThrow(detailCategoryRepository,
        updateItemDto.getDetailCategoryId(), "Invalid Detail Category ID");
    Category category = findByIdOrThrow(categoryRepository, updateItemDto.getCategoryId(),
        "Invalid Category ID");
    Color color = findByIdOrThrow(colorRepository, updateItemDto.getColorId(), "Invalid Color ID");
    Size size = findByIdOrThrow(sizeRepository, updateItemDto.getSizeId(), "Invalid Size ID");

    item.updateItem(ResponseUpdateItemDto.builder()
        .id(item.getId())
        .itemName(updateItemDto.getItemName())
        .description(updateItemDto.getDescription())
        .detailCategory(detailCategory)
        .category(category)
        .sizeId(updateItemDto.getSizeId())
        .colorId(updateItemDto.getColorId())
        .price(updateItemDto.getPrice())
        .thumbnailImage(thumbnailImageEntity)
        .detailImages(detailImageRepository.findAllByItemId(item.getId()))
        .build());
  }


  private ThumbnailImage saveThumbnailImage(MultipartFile thumbnailImage, Item item)
      throws IOException {
    String savedFileName = createSaveFileName(thumbnailImage.getOriginalFilename());
    thumbnailImage.transferTo(new File(getFullPath(savedFileName)));

    ThumbnailImage thumbnailImageEntity = ThumbnailImage.builder()
        .uploadImageName(thumbnailImage.getOriginalFilename())
        .storeImageName(savedFileName)
        .fileSize(thumbnailImage.getSize())
        .item(item)
        .build();

    return thumbnailImageRepository.save(thumbnailImageEntity);
  }

  private void saveDetailImages(List<MultipartFile> detailImages, Item item) throws IOException {
    for (MultipartFile detailImage : detailImages) {
      validateImageType(detailImage.getContentType());

      String savedFileName = createSaveFileName(detailImage.getOriginalFilename());
      detailImage.transferTo(new File(getFullPath(savedFileName)));

      DetailImage detailImageEntity = DetailImage.builder()
          .uploadImageName(detailImage.getOriginalFilename())
          .storeImageName(savedFileName)
          .fileSize(detailImage.getSize())
          .item(item)
          .build();

      detailImageRepository.save(detailImageEntity);
    }
  }

  private Item createNewItem(CreateItemDto dto, DetailCategory detailCategory, Category category,
      Color color, Size size) {
    Item item = Item.builder()
        .itemName(dto.getItemName())
        .detailCategory(detailCategory)
        .category(category)
        .description(dto.getDescription())
        .price(dto.getPrice())
        .color(color)
        .size(size)
        .build();

    return itemRepository.save(item);
  }

  private <T> T findByIdOrThrow(JpaRepository<T, Long> repository, Long id, String errorMessage) {
    return repository.findById(id).orElseThrow(() -> new IllegalArgumentException(errorMessage));
  }

  private void validateThumbnailImage(MultipartFile thumbnailImage) {
    if (thumbnailImage == null || thumbnailImage.isEmpty()) {
      throw new IllegalArgumentException("Thumbnail image is required.");
    }

    if (isValidImageType(thumbnailImage.getContentType())) {
      throw new IllegalArgumentException("Invalid thumbnail image type.");
    }
  }

  private void validateImageType(String contentType) {
    if (isValidImageType(contentType)) {
      throw new IllegalArgumentException("Invalid image type.");
    }
  }

  private boolean isValidImageType(String contentType) {
    return !"image/jpeg".equals(contentType) && !"image/png".equals(contentType)
        && !"image/gif".equals(contentType);
  }

  private String createSaveFileName(String originalFileName) {
    String ext = extractExt(originalFileName);
    String uuid = UUID.randomUUID().toString();
    return uuid + "." + ext;
  }

  private String extractExt(String originalFileName) {
    int index = originalFileName.lastIndexOf(".");
    return originalFileName.substring(index + 1);
  }

  private String getFullPath(String fileName) {
    return fileDir + fileName;
  }

  private String comma(int value) {
    DecimalFormat df = new DecimalFormat("###,###");
    return df.format(value);
  }


}
