package Project.commercial.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPaymentMethod is a Querydsl query type for PaymentMethod
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentMethod extends EntityPathBase<PaymentMethod> {

    private static final long serialVersionUID = 743888876L;

    public static final QPaymentMethod paymentMethod = new QPaymentMethod("paymentMethod");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QPaymentMethod(String variable) {
        super(PaymentMethod.class, forVariable(variable));
    }

    public QPaymentMethod(Path<? extends PaymentMethod> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPaymentMethod(PathMetadata metadata) {
        super(PaymentMethod.class, metadata);
    }

}

