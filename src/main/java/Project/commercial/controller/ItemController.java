package Project.commercial.controller;

import Project.commercial.Dto.*;
import Project.commercial.domain.Item;
import Project.commercial.service.ItemService;
import com.querydsl.core.QueryResults;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/item/create")
    @ResponseBody
    public ItemCreateResponseDto create(@RequestPart ItemCreateRequestDto itemCreateRequestDto,
                                        @RequestPart MultipartFile thumbnail_image,
                                        @RequestPart(required = false) List<MultipartFile> detail_images) throws IOException
    {
        return itemService.create(itemCreateRequestDto,thumbnail_image ,detail_images);
    }

    @GetMapping("/item/list/detail")
    @ResponseBody
    public ItemDto detailPage(@RequestBody Map<String, Long> item_id_map)
    {
        return itemService.detailPage(item_id_map);
    }

    @GetMapping("/item/list")
    @ResponseBody
    public List<ItemDto> list(@PageableDefault(size = 5,sort = "id", direction = Sort.Direction.ASC) Pageable pageable)
    {
        return itemService.list(pageable);
    }

    @GetMapping("/item/list/search")
    @ResponseBody
    public List<ItemDto> search(@RequestBody ItemSearchConditionDto itemSearchConditionDto, @PageableDefault(size = 5,sort = "id", direction = Sort.Direction.ASC) Pageable pageable)
    {
        return itemService.search(itemSearchConditionDto, pageable);
    }

    @DeleteMapping("/item/delete")
    @ResponseBody
    public String delete(@RequestBody Map<String, Long> item_id_map)
    {
        itemService.delete(item_id_map);
        return "Item Delete Done";
    }

    @PatchMapping("/item/modified")
    @ResponseBody
    public ItemModifiedResponseDto modified(@RequestPart ItemModifiedRequestDto itemModifiedRequestDto,
                                            @RequestPart (required = false) MultipartFile new_thumbnail_image,
                                            @RequestPart(required = false) List<MultipartFile> detail_images ) throws IOException
    {
       return  itemService.modified(itemModifiedRequestDto,new_thumbnail_image ,detail_images);
    }

    }
