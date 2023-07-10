package Project.commercial.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrderStatus is a Querydsl query type for OrderStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderStatus extends EntityPathBase<OrderStatus> {

    private static final long serialVersionUID = 534588293L;

    public static final QOrderStatus orderStatus = new QOrderStatus("orderStatus");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QOrderStatus(String variable) {
        super(OrderStatus.class, forVariable(variable));
    }

    public QOrderStatus(Path<? extends OrderStatus> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrderStatus(PathMetadata metadata) {
        super(OrderStatus.class, metadata);
    }

}

