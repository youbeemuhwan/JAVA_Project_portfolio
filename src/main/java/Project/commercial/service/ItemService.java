package Project.commercial.service;

import Project.commercial.Dto.*;
import Project.commercial.domain.*;
import Project.commercial.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Value("$(file.dir)")
    String fileDir;

    public ItemCreateResponseDto create(ItemCreateRequestDto itemCreateRequestDto, List<MultipartFile> files) throws IOException {

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

        Item saveItem = itemRepository.save(entity);

        if (!CollectionUtils.isEmpty(files)) {
            extractFile(files, saveItem);
        }
        
        List<DetailImage> detailImageList = detailImageRepository.findAllByItem_id(saveItem.getId());

        return ItemCreateResponseDto.builder()
                .id(saveItem.getId())
                .category(saveItem.getCategory())
                .detailCategory(saveItem.getDetailCategory())
                .itemName(saveItem.getItemName())
                .description(saveItem.getDescription())
                .color(saveItem.getColor())
                .size(saveItem.getSize())
                .price(comma(saveItem.getPrice()))
                .detailImage(detailImageList)
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
                    .detailImage(item.getDetailImage())
                    .build();

            itemDtoList.add(build);
        }

        return itemDtoList;
    }

    public ItemDto detailPage(Map<String, Long> item_id_map) {
        Long itemId = item_id_map.get("item_id");
        Item item = itemRepository.findById(itemId).orElseThrow();

        ItemDto buildDetailPage = ItemDto.builder()
                .id(item.getId())
                .category(item.getCategory())
                .detailCategory(item.getDetailCategory())
                .itemName(item.getItemName())
                .description(item.getDescription())
                .color(item.getColor())
                .size(item.getSize())
                .price(comma(item.getPrice()))
                .detailImage(item.getDetailImage())
                .build();

        return buildDetailPage;
    }

    public void delete(Map<String, Long> item_id_map) {
        Long item_id = item_id_map.get("item_id");
        Item item = itemRepository.findById(item_id).orElseThrow(
                () -> new RuntimeException("해당 아이템은 존재하지 않습니다.")
        );

        itemRepository.delete(item);
    }


    public ItemModifiedResponseDto modified(ItemModifiedRequestDto itemModifiedRequestDto, List<MultipartFile> files) throws IOException {

        Item item = itemRepository.findById(itemModifiedRequestDto.getId()).orElseThrow(
                () -> new RuntimeException("해당 아이템은 존재하지 않습니다.")
        );

        DetailCategory detailCategory = detailCategoryRepository.findById(itemModifiedRequestDto.getDetailCategory_id()).orElseThrow();
        Category category = categoryRepository.findById(itemModifiedRequestDto.getCategory_id()).orElseThrow();
        Color color = colorRepository.findById(itemModifiedRequestDto.getColor_id()).orElseThrow();
        Size size = sizeRepository.findById(itemModifiedRequestDto.getSize_id()).orElseThrow();

        List<DetailImage> detailImages = item.getDetailImage();

        if (CollectionUtils.isEmpty(detailImages) && !CollectionUtils.isEmpty(files)) {

            extractFile(files, item);

            log.info("케이스 1 : 상세이미지는 없고 파일은 있을때!");

            ItemModifiedResponseDto itemModifiedResponseDto = ItemModifiedResponseDto.builder()
                    .id(item.getId())
                    .itemName(itemModifiedRequestDto.getItemName())
                    .description(itemModifiedRequestDto.getDescription())
                    .detailCategory(detailCategory)
                    .category(category)
                    .size(size)
                    .color(color)
                    .price(itemModifiedRequestDto.getPrice())
                    .detailImage(detailImageRepository.findAllByItem_id(item.getId()))
                    .build();

            item.updateItem(itemModifiedResponseDto);

            return itemModifiedResponseDto;
        }

        if (!CollectionUtils.isEmpty(detailImages) && CollectionUtils.isEmpty(files)) {
            log.info("케이스 2 : 상세이미지는 있는데 파일은 없을때!");

            detailImageRepository.deleteByItem_id(item.getId());

            ItemModifiedResponseDto itemModifiedResponseDto = ItemModifiedResponseDto.builder()
                    .id(item.getId())
                    .itemName(itemModifiedRequestDto.getItemName())
                    .description(itemModifiedRequestDto.getDescription())
                    .detailCategory(detailCategory)
                    .category(category)
                    .size(size)
                    .color(color)
                    .price(itemModifiedRequestDto.getPrice())
                    .detailImage(detailImageRepository.findAllByItem_id(item.getId()))
                    .build();

            item.updateItem(itemModifiedResponseDto);

            return itemModifiedResponseDto;

        }

        if (!CollectionUtils.isEmpty(detailImages) && !CollectionUtils.isEmpty(files)) {

            log.info("케이스 3 : 상세이미지 와 파일 모두 존재 할때");

            detailImageRepository.deleteByItem_id(item.getId());

            for (MultipartFile file : files) {
                String originalFilename = file.getOriginalFilename();
                String saveFileName = createSaveFileName(originalFilename);

                long fileSizeFile = file.getSize();

                file.transferTo(new File(getFullPath(saveFileName)));

                DetailImageRequestDto detailImageRequestDto = DetailImageRequestDto.builder()
                        .storeImageName(saveFileName)
                        .uploadImageName(originalFilename)
                        .fileSize(fileSizeFile)
                        .item(item)
                        .build();

                detailImageRepository.save(detailImageRequestDto.toEntity(detailImageRequestDto));
            }

            ItemModifiedResponseDto itemModifiedResponseDto = ItemModifiedResponseDto.builder()
                    .id(item.getId())
                    .itemName(itemModifiedRequestDto.getItemName())
                    .description(itemModifiedRequestDto.getDescription())
                    .detailCategory(detailCategory)
                    .category(category)
                    .size(size)
                    .color(color)
                    .price(itemModifiedRequestDto.getPrice())
                    .detailImage(detailImageRepository.findAllByItem_id(item.getId()))
                    .build();

            item.updateItem(itemModifiedResponseDto);

            return itemModifiedResponseDto;
        }
        ItemModifiedResponseDto itemModifiedResponseDto = ItemModifiedResponseDto.builder()
                .id(item.getId())
                .itemName(itemModifiedRequestDto.getItemName())
                .description(itemModifiedRequestDto.getDescription())
                .detailCategory(detailCategory)
                .category(category)
                .size(size)
                .color(color)
                .price(itemModifiedRequestDto.getPrice())
                .detailImage(item.getDetailImage())
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
        String value_str = df.format(value);
        return value_str;
    }

    public int discount(int price, int discountRate){
        double discount = discountRate / 100;
        int resultPrice = (int) (price * discount);
        return resultPrice;

    }

    private void extractFile(@Nullable List<MultipartFile> files, Item saveItem) throws IOException {
        for (MultipartFile file : files) {
            if (!(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")
                    || file.getContentType().equals("image/gif"))) {
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
