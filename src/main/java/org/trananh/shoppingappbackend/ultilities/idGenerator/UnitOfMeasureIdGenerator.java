package org.trananh.shoppingappbackend.ultilities.idGenerator;

import java.util.stream.Stream;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;


public class UnitOfMeasureIdGenerator implements IdentifierGenerator{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4931467457985073156L;

	@Override
	public Object generate(SharedSessionContractImplementor session, Object object) {
		String query = "SELECT id FROM UnitOfMeasure";
		Stream<Integer> ids = session.createQuery(query, Integer.class).stream();
		int max = ids.max(Integer::compare).get();
		return max + 1;
	}
}