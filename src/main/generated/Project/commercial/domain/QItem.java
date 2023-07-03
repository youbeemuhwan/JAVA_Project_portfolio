package Project.commercial.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = -141828082L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final QCategory category;

    public final QColor color;

    public final StringPath description = createString("description");

    public final QDetailCategory detailCategory;

    public final ListPath<DetailImage, QDetailImage> detailImage = this.<DetailImage, QDetailImage>createList("detailImage", DetailImage.class, QDetailImage.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath itemName = createString("itemName");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final QSize size;

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category")) : null;
        this.color = inits.isInitialized("color") ? new QColor(forProperty("color")) : null;
        this.detailCategory = inits.isInitialized("detailCategory") ? new QDetailCategory(forProperty("detailCategory")) : null;
        this.size = inits.isInitialized("size") ? new QSize(forProperty("size")) : null;
    }

}

