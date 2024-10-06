package Project.commercial.controller;

import Project.commercial.dto.item.*;
import Project.commercial.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping()
    @ResponseBody
    public ResponseItemDto createItem(@RequestPart CreateItemDto createItemDto,
                                      @RequestPart MultipartFile thumbnail_image,
                                      @RequestPart(required = false) List<MultipartFile> detail_images) throws IOException
    {
        return itemService.createItem(createItemDto,thumbnail_image ,detail_images);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ItemDto getItemDetail(@PathVariable Long id)
    {
        return itemService.getItemDetail(id);
    }

    @GetMapping()
    @ResponseBody
    public List<ItemDto> getItems(@PageableDefault(size = 5,sort = "id", direction = Sort.Direction.ASC) Pageable pageable)
    {
        return itemService.getItems(pageable);
    }

    @GetMapping("/search")
    @ResponseBody
    public ItemSearchResponseDto getItemsBySearch(@RequestBody ItemSearchConditionDto itemSearchConditionDto, @PageableDefault(size = 5,sort = "id", direction = Sort.Direction.ASC) Pageable pageable)
    {
        return itemService.getItemsBySearch(itemSearchConditionDto, pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteItem(@PathVariable() Long id)
    {
        itemService.deleteItem(id);
        return "Item Delete Done";
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public void updateItem(@PathVariable() Long id, @RequestPart UpdateItemDto updateItemDto,
                                            @RequestPart (required = false) MultipartFile newThumbnailImage,
                                            @RequestPart(required = false) List<MultipartFile> detailImages ) throws IOException
    {
        itemService.updateItem(id, updateItemDto,newThumbnailImage ,detailImages);
    }

    }
