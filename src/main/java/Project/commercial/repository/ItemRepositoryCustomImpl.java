package Project.commercial.repository;

import Project.commercial.dto.item.ItemSearchConditionDto;
import Project.commercial.domain.Item;
import Project.commercial.domain.QItem;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.List;

import static Project.commercial.domain.QItem.item;

@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Item> searchItem(ItemSearchConditionDto itemSearchConditionDto, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(item)
                .where(
                        categoryEq(itemSearchConditionDto.getCategoryId()),
                        detailCategoryEq(itemSearchConditionDto.getDetailCategoryId()),
                        colorEq(itemSearchConditionDto.getColorId()),
                        sizeEq(itemSearchConditionDto.getSizeId()),
                        itemNameContain(itemSearchConditionDto.getItemName()),
                        priceBetween(itemSearchConditionDto.getMinimumAmount(), itemSearchConditionDto.getMaxAmount())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(item.id.asc())
                .fetch();
    }

    public Long getSearchItemCount(ItemSearchConditionDto itemSearchConditionDto){
         return jpaQueryFactory
                .select(item.count())
                .from(item)
                .where(
                        categoryEq(itemSearchConditionDto.getCategoryId()),
                        detailCategoryEq(itemSearchConditionDto.getDetailCategoryId()),
                        colorEq(itemSearchConditionDto.getColorId()),
                        sizeEq(itemSearchConditionDto.getSizeId()),
                        itemNameContain(itemSearchConditionDto.getItemName()),
                        priceBetween(itemSearchConditionDto.getMinimumAmount(), itemSearchConditionDto.getMaxAmount())
                )
                .fetchOne();
    }
    
    

    private BooleanExpression itemNameContain(String username){
        if(StringUtils.isNullOrEmpty(username))
        {
            return null;
        }
        return item.itemName.contains(username);
    }
    private BooleanExpression detailCategoryEq(Long detailCategory_id){
        if(detailCategory_id == null)
        {
            return null;
        }
        return item.detailCategory.id.eq(detailCategory_id);
    }

    private BooleanExpression colorEq(Long color_id){
        if(color_id == null)
        {
            return null;
        }
        return item.color.id.eq(color_id);
    }

    private BooleanExpression sizeEq(Long size_id){
        if(size_id == null)
        {
            return null;
        }

        return item.size.id.eq(size_id);
    }

    private BooleanExpression categoryEq(Long category_id){
        if(category_id == null)
        {
            return null;
        }
        return item.category.id.eq(category_id);
    }

    private BooleanExpression priceBetween(@Nullable Integer minimumPrice, @Nullable Integer maxPrice){

       if (minimumPrice == null && maxPrice == null){
           return item.price.goe(0);
       }

       if (!(minimumPrice == null) && (maxPrice == null)){
           return item.price.goe(minimumPrice);
       }

       if (minimumPrice == null){
           return item.price.loe(maxPrice);
       }

        return item.price.between(minimumPrice, maxPrice);
    }
}
