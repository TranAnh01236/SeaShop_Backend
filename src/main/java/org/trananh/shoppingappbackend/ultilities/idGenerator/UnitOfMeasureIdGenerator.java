package org.trananh.shoppingappbackend.ultilities.idGenerator;

import java.util.Collections;
import java.util.List;
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
		List<Integer> ids = session.createQuery(query, Integer.class).list();
		if (ids == null || ids.size() == 0) {
			return 1;
		}
		Collections.sort(ids);
		return ids.get(ids.size() - 1) + 1;
	}
}