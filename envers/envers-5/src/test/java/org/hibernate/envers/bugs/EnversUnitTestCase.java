/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.envers.bugs;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.UUID;

/**
 * This template demonstrates how to develop a test case for Hibernate Envers, using
 * its built-in unit test framework.
 */
public class EnversUnitTestCase extends AbstractEnversTestCase {

	// Add your entities here.
	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				Item.class, CustomField.class
//				Foo.class,
//				Bar.class
		};
	}

	// If you use *.hbm.xml mappings, instead of annotations, add the mappings here.
	@Override
	protected String[] getMappings() {
		return new String[] {
//				"Foo.hbm.xml",
//				"Bar.hbm.xml"
		};
	}
	// If those mappings reside somewhere other than resources/org/hibernate/test, change this.
	@Override
	protected String getBaseForMappings() {
		return "org/hibernate/test/";
	}

	// Add in any settings that are specific to your test.  See resources/hibernate.properties for the defaults.
	@Override
	protected void configure(Configuration configuration) {
		super.configure( configuration );

		configuration.setProperty( AvailableSettings.SHOW_SQL, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString() );
		//configuration.setProperty( AvailableSettings.GENERATE_STATISTICS, "true" );
	}

	// Add your tests, using standard JUnit.
	@Test
	@Transactional
	public void hhh123Test() throws Exception {
		//AuditReader reader = getAuditReader();
		Session session = openSession();
		// Do stuff...

		Item item = new Item();
		item.setGuid(UUID.randomUUID().toString());

		Transaction tx = session.beginTransaction();
		Serializable id = session.save(item);
		tx.commit();

		CustomField field = new CustomField();
		field.setFieldName("test_field");
		field.setFieldValue("test");
		field.setItem(item);
		item.getCustomFields().put(field.getFieldName(), field);

		Transaction tx2 = session.beginTransaction();
		session.persist(item);
		tx2.commit();

		Transaction tx3 = session.beginTransaction();
		Item item2 = session.get(Item.class, id);
		CustomField field2 = new CustomField();
		field2.setFieldName("test_field_2");
		field2.setFieldValue("test_2");
		field2.setItem(item2);
		item.getCustomFields().put(field2.getFieldName(), field2);
		session.persist(item2);
		tx3.commit();

		Transaction tx4 = session.beginTransaction();
		Item item3 = session.get(Item.class, id);
		CustomField field3 = new CustomField();
		field3.setFieldName("test_field_3");
		field3.setFieldValue("test_3");
		field3.setItem(item3);
		item.getCustomFields().put(field3.getFieldName(), field3);
		session.persist(item3);
		tx4.commit();
	}
}
