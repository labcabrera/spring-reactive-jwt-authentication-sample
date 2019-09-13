package com.github.labcabrera.jwt.sample.rsql;

import java.util.function.BiFunction;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import com.github.rutledgepaulv.rqe.pipes.DefaultArgumentConversionPipe;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;

@Component
public class RsqlParser implements BiFunction<String, Class<?>, Query> {

	protected final QueryConversionPipeline pipeline;
	protected final MongoVisitor mongoVisitor;

	public RsqlParser() {
		DefaultArgumentConversionPipe pipe = DefaultArgumentConversionPipe.builder()
			.useNonDefaultStringToTypeConverter(new CustomSpringConversionServiceConverter())
			.build();
		pipeline = QueryConversionPipeline.builder()
			.useNonDefaultArgumentConversionPipe(pipe)
			.build();
		mongoVisitor = new CaseInsensitiveMongoVisitor();
	}

	@Override
	public Query apply(String searchExpression, Class<?> entityClass) {
		Query query = new Query();
		if (StringUtils.isNotBlank(searchExpression)) {
			Condition<GeneralQueryBuilder> condition = pipeline.apply(searchExpression, entityClass);
			Criteria criteria = condition.query(mongoVisitor);
			query.addCriteria(criteria);
		}
		return query;
	}

	public Criteria getCriteria(String searchExpression, Class<?> entityClass) {
		if (StringUtils.isNoneBlank(searchExpression)) {
			Condition<GeneralQueryBuilder> condition = pipeline.apply(searchExpression, entityClass);
			return condition.query(mongoVisitor);
		}
		return null;
	}

}
