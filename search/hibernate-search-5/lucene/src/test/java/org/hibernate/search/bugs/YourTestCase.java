package org.hibernate.search.bugs;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.testsupport.TestForIssue;
import org.junit.Test;

public class YourTestCase extends SearchTestBase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[]{
				ItemEntity.class, ItemVersionEntity.class, ItemNodeEntity.class };
	}

	@Test
	@TestForIssue(jiraKey = "HSEARCH-3647") // Please fill in the JIRA key of your issue
	@SuppressWarnings("unchecked")
	public void testYourBug() {
		try ( Session s = getSessionFactory().withOptions().tenantIdentifier("test").openSession() ) {

			// PREPARE
			ItemEntity item = new ItemEntity( 1L, "example" );

			ItemVersionEntity version = new ItemVersionEntity();
			version.setItem(item);
			item.getVersions().add(version);

			ItemNodeEntity node1 = new ItemNodeEntity();
			node1.setText("foobar");
			node1.setVersion(version);
			version.getNodes().add(node1);

			Transaction tx = s.beginTransaction();
			s.persist( item );
			tx.commit();

			{
				FullTextSession session = Search.getFullTextSession(s);
				QueryBuilder qb = session.getSearchFactory().buildQueryBuilder().forEntity(ItemEntity.class).get();
				Query query = qb.keyword()
						.onFields("name", "versions.nodes.text")
						.matching("foobar")
						.createQuery();

				List<ItemEntity> result = (List<ItemEntity>) session.createFullTextQuery(query).list();
				assertEquals(1, result.size());
				assertEquals(1l, (long) result.get(0).getId());
			}

			// ADD NEW NODE
			ItemVersionEntity updatedVersion = new ItemVersionEntity();
			updatedVersion.setId(version.getId());
			updatedVersion.setItem(item);

			ItemNodeEntity node2 = new ItemNodeEntity();
			node2.setText("pizza");
			node2.setVersion(updatedVersion);

			updatedVersion.getNodes().add(node1);
			updatedVersion.getNodes().add(node2);

			Transaction tx2 = s.beginTransaction();
			s.merge( updatedVersion );
			tx2.commit();

			{
				FullTextSession session = Search.getFullTextSession(s);
				QueryBuilder qb = session.getSearchFactory().buildQueryBuilder().forEntity(ItemEntity.class).get();
				Query query = qb.keyword()
						.onFields("name", "versions.nodes.text")
						.matching("foobar")
						.createQuery();

				List<ItemEntity> result = (List<ItemEntity>) session.createFullTextQuery(query).list();
				assertEquals(1, result.size());
				assertEquals(1l, (long) result.get(0).getId());
			}
		}
	}
}
