package Project.commercial.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDetailCategory is a Querydsl query type for DetailCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDetailCategory extends EntityPathBase<DetailCategory> {

    private static final long serialVersionUID = 1648295850L;

    public static final QDetailCategory detailCategory = new QDetailCategory("detailCategory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QDetailCategory(String variable) {
        super(DetailCategory.class, forVariable(variable));
    }

    public QDetailCategory(Path<? extends DetailCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDetailCategory(PathMetadata metadata) {
        super(DetailCategory.class, metadata);
    }

}

