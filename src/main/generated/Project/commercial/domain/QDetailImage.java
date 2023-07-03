package Project.commercial.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDetailImage is a Querydsl query type for DetailImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDetailImage extends EntityPathBase<DetailImage> {

    private static final long serialVersionUID = 1128875567L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDetailImage detailImage = new QDetailImage("detailImage");

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final StringPath storeImageName = createString("storeImageName");

    public final StringPath uploadImageName = createString("uploadImageName");

    public QDetailImage(String variable) {
        this(DetailImage.class, forVariable(variable), INITS);
    }

    public QDetailImage(Path<? extends DetailImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDetailImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDetailImage(PathMetadata metadata, PathInits inits) {
        this(DetailImage.class, metadata, inits);
    }

    public QDetailImage(Class<? extends DetailImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item"), inits.get("item")) : null;
    }

}

