package Project.commercial.service;

import Project.commercial.Dto.*;
import Project.commercial.domain.*;
import Project.commercial.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
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

    public ItemCreateResponseDto create(ItemCreateRequestDto itemCreateRequestDto,MultipartFile thumbnail_image ,List<MultipartFile> detail_images) throws IOException {

        if(thumbnail_image.isEmpty())
        {
            throw new RuntimeException("썸네일 이미지는 필수값 입니다.");
        }

        DetailCategory detailCategory_id = detailCategoryRepository.findById(itemCreateRequestDto.getDetailCategory_id()).orElseThrow();
        Category category_id = categoryRepository.findById(itemCreateRequestDto.getCategory_id()).orElseThrow();
        Color color_id = colorRepository.findById(itemCreateRequestDto.getColor_id()).orElseThrow();
        Size size_id = sizeRepository.findById(itemCreateRequestDto.getSize_id()).orElseThrow();

        Item entity = Item.builder()
                .itemName(itemCreateRequestDto.getItemName())
                .detailCategory(detailCategory_id)
                .category(category_id)
                .description(itemCreateRequestDto.getDescription())
                .price(itemCreateRequestDto.getPrice())
                .color(color_id)
                .size(size_id)
                .build();

        Item savedItem = itemRepository.save(entity);

        ThumbnailImageRequestDto thumbnailImageRequestDto = ThumbnailImageRequestDto.builder()
                .uploadImageName(thumbnail_image.getOriginalFilename())
                .storeImageName(createSaveFileName(thumbnail_image.getOriginalFilename()))
                .fileSize(thumbnail_image.getSize())
                .item(savedItem)
                .build();

        thumbnail_image.transferTo(new File(getFullPath(createSaveFileName(thumbnail_image.getOriginalFilename()))));

        ThumbnailImage thumbnailImageRequestDtoEntity = thumbnailImageRequestDto.toEntity(thumbnailImageRequestDto);
        thumbnailImageRepository.save(thumbnailImageRequestDtoEntity);

        if (!CollectionUtils.isEmpty(detail_images)) {
            extractFile(detail_images, savedItem);
        }

        List<DetailImage> detailImageList = detailImageRepository.findAllByItem_id(savedItem.getId());
        ThumbnailImage savedItemThumbnailImage = thumbnailImageRepository.findByItem_id(savedItem.getId()).orElseThrow();

        return ItemCreateResponseDto.builder()
                .id(savedItem.getId())
                .category(savedItem.getCategory())
                .detailCategory(savedItem.getDetailCategory())
                .itemName(savedItem.getItemName())
                .description(savedItem.getDescription())
                .color(savedItem.getColor())
                .size(savedItem.getSize())
                .price(comma(savedItem.getPrice()))
                .detailImage(detailImageList)
                .thumbnailImage(savedItemThumbnailImage)
                .build();
    }
    public List<ItemDto> list(Pageable pageable) {
        Page<Item> items = itemRepository.findAll(pageable);
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (Item item : items) {

            ItemDto build = ItemDto.builder()
                    .id(item.getId())
                    .category(item.getCategory())
                    .detailCategory(item.getDetailCategory())
                    .itemName(item.getItemName())
                    .description(item.getDescription())
                    .size(item.getSize())
                    .color(item.getColor())
                    .price(comma(item.getPrice()))
                    .thumbnailImage(item.getThumbnailImage())
                    .build();

            itemDtoList.add(build);
        }
        return itemDtoList;
    }

    public List<ItemDto> search(ItemSearchConditionDto itemSearchConditionDto, Pageable pageable){
        List<Item> itemList = itemRepository.searchItem(itemSearchConditionDto, pageable);
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (Item item : itemList){

            ItemDto itemDto = ItemDto.builder()
                    .id(item.getId())
                    .category(item.getCategory())
                    .detailCategory(item.getDetailCategory())
                    .itemName(item.getItemName())
                    .description(item.getDescription())
                    .color(item.getColor())
                    .size(item.getSize())
                    .price(comma(item.getPrice()))
                    .thumbnailImage(item.getThumbnailImage())
                    .build();

            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }

    public ItemDto detailPage(Map<String, Long> item_id_map) {
        Long itemId = item_id_map.get("item_id");
        Item item = itemRepository.findById(itemId).orElseThrow();



        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .category(item.getCategory())
                .detailCategory(item.getDetailCategory())
                .itemName(item.getItemName())
                .description(item.getDescription())
                .color(item.getColor())
                .size(item.getSize())
                .price(comma(item.getPrice()))
                .detailImage(item.getDetailImage())
                .thumbnailImage(item.getThumbnailImage())
                .build();


        return  itemDto;
    }

    public void delete(Map<String, Long> item_id_map) {
        Long item_id = item_id_map.get("item_id");
        Item item = itemRepository.findById(item_id).orElseThrow(
                () -> new RuntimeException("해당 아이템은 존재하지 않습니다."));

        itemRepository.delete(item);
    }

    public ItemModifiedResponseDto modified(ItemModifiedRequestDto itemModifiedRequestDto, MultipartFile new_thumbnail_image ,List<MultipartFile> new_detail_images) throws IOException {
        Item item = itemRepository.findById(itemModifiedRequestDto.getId()).orElseThrow(
                () -> new RuntimeException("해당 아이템은 존재하지 않습니다.")
        );

        if(new_thumbnail_image == null) {
            throw new RuntimeException("썸네일 이미지는 필수 값 입니다.");
        }

        if (!(Objects.equals(new_thumbnail_image.getContentType(), "image/jpeg") || Objects.equals(new_thumbnail_image.getContentType(), "image/png")
                || Objects.equals(new_thumbnail_image.getContentType(), "image/gif"))) {
            throw new RuntimeException("해당 첨부파일 형식이 올바르지 않습니다.");
        }

        if(thumbnailImageRepository.findByItem_id(item.getId()).isPresent()){
            thumbnailImageRepository.deleteByItem_id(item.getId());
        }

            ThumbnailImageRequestDto thumbnailImageRequestDto = ThumbnailImageRequestDto.builder()
                    .uploadImageName(new_thumbnail_image.getOriginalFilename())
                    .storeImageName(createSaveFileName(new_thumbnail_image.getOriginalFilename()))
                    .fileSize(new_thumbnail_image.getSize())
                    .item(item)
                    .build();

            thumbnailImageRepository.save(thumbnailImageRequestDto.toEntity(thumbnailImageRequestDto));
            thumbnailImageRepository.flush();

        DetailCategory detailCategory = detailCategoryRepository.findById(itemModifiedRequestDto.getDetailCategory_id()).orElseThrow();
        Category category = categoryRepository.findById(itemModifiedRequestDto.getCategory_id()).orElseThrow();
        Color color = colorRepository.findById(itemModifiedRequestDto.getColor_id()).orElseThrow();
        Size size = sizeRepository.findById(itemModifiedRequestDto.getSize_id()).orElseThrow();
        Item newItem = itemRepository.findById(itemModifiedRequestDto.getId()).orElseThrow();

        List<DetailImage> exist_detailImages = newItem.getDetailImage();

        if (CollectionUtils.isEmpty(exist_detailImages) && !CollectionUtils.isEmpty(new_detail_images)){

            extractFile(new_detail_images, newItem);

            log.info("케이스 1 : 현재 저장된 상세이미지는 없고 수정을 위한 상세이미지 파일은 존재할때");

            ItemModifiedResponseDto itemModifiedResponseDto = ItemModifiedResponseDto.builder()
                    .id(newItem.getId())
                    .itemName(itemModifiedRequestDto.getItemName())
                    .description(itemModifiedRequestDto.getDescription())
                    .detailCategory(detailCategory)
                    .category(category)
                    .size(size)
                    .color(color)
                    .price(itemModifiedRequestDto.getPrice())
                    .detailImage(detailImageRepository.findAllByItem_id(item.getId()))
                    .thumbnailImage(thumbnailImageRepository.findByItem_id(item.getId()).orElseThrow())
                    .build();

            item.updateItem(itemModifiedResponseDto);

            return itemModifiedResponseDto;
        }

        if (!CollectionUtils.isEmpty(exist_detailImages) && CollectionUtils.isEmpty(new_detail_images)) {
            log.info("케이스 2 : 현재 저장된 상세이미지는 있는데 수정을 위한 상세이미지 파일은 존재하지 않을때");

            detailImageRepository.deleteByItem_id(newItem.getId());

            ItemModifiedResponseDto itemModifiedResponseDto = ItemModifiedResponseDto.builder()
                    .id(newItem.getId())
                    .itemName(itemModifiedRequestDto.getItemName())
                    .description(itemModifiedRequestDto.getDescription())
                    .detailCategory(detailCategory)
                    .category(category)
                    .size(size)
                    .color(color)
                    .price(itemModifiedRequestDto.getPrice())
                    .detailImage(detailImageRepository.findAllByItem_id(newItem.getId()))
                    .thumbnailImage(thumbnailImageRepository.findByItem_id(newItem.getId()).orElseThrow())
                    .build();

            item.updateItem(itemModifiedResponseDto);

            return itemModifiedResponseDto;

        }

        if (!CollectionUtils.isEmpty(exist_detailImages) && !CollectionUtils.isEmpty(new_detail_images)) {

            log.info("케이스 3 : 저장된 상세이미지 와 수정을 위한 상세이미지 파일 모두 존재할때");

            detailImageRepository.deleteByItem_id(newItem.getId());


            for (MultipartFile new_detail_image : new_detail_images) {
                String originalFilename = new_detail_image.getOriginalFilename();
                String saveFileName = createSaveFileName(originalFilename);


                long fileSizeFile = new_detail_image.getSize();

                new_detail_image.transferTo(new File(getFullPath(saveFileName)));

                DetailImageRequestDto detailImageRequestDto = DetailImageRequestDto.builder()
                        .storeImageName(saveFileName)
                        .uploadImageName(originalFilename)
                        .fileSize(fileSizeFile)
                        .item(newItem)
                        .build();

                detailImageRepository.save(detailImageRequestDto.toEntity(detailImageRequestDto));
            }

            ItemModifiedResponseDto itemModifiedResponseDto = ItemModifiedResponseDto.builder()
                    .id(newItem.getId())
                    .itemName(itemModifiedRequestDto.getItemName())
                    .description(itemModifiedRequestDto.getDescription())
                    .detailCategory(detailCategory)
                    .category(category)
                    .size(size)
                    .color(color)
                    .price(itemModifiedRequestDto.getPrice())
                    .detailImage(detailImageRepository.findAllByItem_id(newItem.getId()))
                    .thumbnailImage(newItem.getThumbnailImage())
                    .build();

            item.updateItem(itemModifiedResponseDto);

            return itemModifiedResponseDto;
        }

        ItemModifiedResponseDto itemModifiedResponseDto = ItemModifiedResponseDto.builder()
                .id(newItem.getId())
                .itemName(itemModifiedRequestDto.getItemName())
                .description(itemModifiedRequestDto.getDescription())
                .detailCategory(detailCategory)
                .category(category)
                .size(size)
                .color(color)
                .price(itemModifiedRequestDto.getPrice())
                .detailImage(newItem.getDetailImage())
                .thumbnailImage(newItem.getThumbnailImage())
                .build();

        item.updateItem(itemModifiedResponseDto);

        return itemModifiedResponseDto;
    }
    private String createSaveFileName (String originalFileName){
            String ext = extractExt(originalFileName);
            String uuid = UUID.randomUUID().toString();

            return uuid + "." + ext;
        }
        private String extractExt (String originalFileName){
            int index = originalFileName.lastIndexOf(".");
            return originalFileName.substring(index + 1);
        }
        private String getFullPath (String fileName){
            return fileDir + fileName;
        }
    public String comma(int value) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(value);
    }

// 상품 상세 이미지 저장 로직
    private void extractFile(List<MultipartFile> files, Item saveItem) throws IOException {
        for (MultipartFile file : files) {

            if (!(Objects.equals(file.getContentType(), "image/jpeg") || Objects.equals(file.getContentType(), "image/png")
                    || Objects.equals(file.getContentType(), "image/gif"))) {
                throw new RuntimeException("해당 첨부파일 형식이 올바르지 않습니다.");
            }
            String originalFilename = file.getOriginalFilename();

            String saveFileName = createSaveFileName(originalFilename);

            long fileSize = file.getSize();

            file.transferTo(new File(getFullPath(saveFileName)));

            DetailImageRequestDto detailImageRequestDto = DetailImageRequestDto.builder()
                    .uploadImageName(originalFilename)
                    .storeImageName(saveFileName)
                    .fileSize(fileSize)
                    .item(saveItem)
                    .build();

            DetailImage detailImageEntity = detailImageRequestDto.toEntity(detailImageRequestDto);
            detailImageRepository.save(detailImageEntity);
        }
    }



}
