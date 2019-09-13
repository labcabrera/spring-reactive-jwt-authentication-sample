package com.github.labcabrera.jwt.sample.rsql;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.query.Criteria;

import com.github.rutledgepaulv.qbuilders.nodes.ComparisonNode;
import com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;

public class CaseInsensitiveMongoVisitor extends MongoVisitor {

	@Override
	protected Criteria visit(ComparisonNode node) {
		ComparisonOperator operator = node.getOperator();

		Collection<?> values = node.getValues().stream().map(normalizer).collect(Collectors.toList());
		String field = node.getField().asKey();

		if (ComparisonOperator.EQ.equals(operator)) {
			return where(field).is(single(values));
		}
		else if (ComparisonOperator.NE.equals(operator)) {
			return where(field).ne(single(values));
		}
		else if (ComparisonOperator.EX.equals(operator)) {
			return where(field).exists((Boolean) single(values));
		}
		else if (ComparisonOperator.GT.equals(operator)) {
			return where(field).gt(single(values));
		}
		else if (ComparisonOperator.LT.equals(operator)) {
			return where(field).lt(single(values));
		}
		else if (ComparisonOperator.GTE.equals(operator)) {
			return where(field).gte(single(values));
		}
		else if (ComparisonOperator.LTE.equals(operator)) {
			return where(field).lte(single(values));
		}
		else if (ComparisonOperator.IN.equals(operator)) {
			return where(field).in(values);
		}
		else if (ComparisonOperator.NIN.equals(operator)) {
			return where(field).nin(values);
		}
		else if (ComparisonOperator.RE.equals(operator)) {
			return where(field).regex((String) single(values), "i");
		}
		else if (ComparisonOperator.SUB_CONDITION_ANY.equals(operator)) {
			return where(field).elemMatch(condition(node));
		}

		throw new UnsupportedOperationException("This visitor does not support the operator " + operator + ".");
	}
}