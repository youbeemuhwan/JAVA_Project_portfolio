package Project.commercial.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QThumbnailImage is a Querydsl query type for ThumbnailImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QThumbnailImage extends EntityPathBase<ThumbnailImage> {

    private static final long serialVersionUID = -1929657590L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QThumbnailImage thumbnailImage = new QThumbnailImage("thumbnailImage");

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final StringPath storeImageName = createString("storeImageName");

    public final StringPath uploadImageName = createString("uploadImageName");

    public QThumbnailImage(String variable) {
        this(ThumbnailImage.class, forVariable(variable), INITS);
    }

    public QThumbnailImage(Path<? extends ThumbnailImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QThumbnailImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QThumbnailImage(PathMetadata metadata, PathInits inits) {
        this(ThumbnailImage.class, metadata, inits);
    }

    public QThumbnailImage(Class<? extends ThumbnailImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item"), inits.get("item")) : null;
    }

}

