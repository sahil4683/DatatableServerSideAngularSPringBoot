package com.sp.aop_demo.jquery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

//@SuppressWarnings("serial")
@Service
public class JqueryDatatablePluginDemo {

	@Autowired
	private PersonRepository personRepository;

	@PersistenceContext
	private EntityManager entityManager;

//	private String GLOBAL_SEARCH_TERM;
	private String COLUMN_NAME;
	private String DIRECTION;
	private int INITIAL;
	private int RECORD_SIZE;
	private String ID_SEARCH_TERM, NAME_SEARCH_TERM, PLACE_SEARCH_TERM, CITY_SEARCH_TERM, STATE_SEARCH_TERM,
			PHONE_SEARCH_TERM;

	public JsonObjectDto doGet(DataTableRequest request) {

		String[] columnNames = { "id", "name", "place", "city", "state", "phone" };

		JsonObjectDto jsonResult = new JsonObjectDto();
		int listDisplayAmount = 10;
		int start = 0;
		int column = 0;
		String dir = "asc";
		String pageNo = request.getIDisplayStart();
		String pageSize = request.getIDisplayLength();
		String colIndex = request.getISortCol();
		String sortDirection = request.getSSortDir();

		if (pageNo != null) {
			start = Integer.parseInt(pageNo);
			if (start < 0) {
				start = 0;
			}
		}
		if (pageSize != null) {
			listDisplayAmount = Integer.parseInt(pageSize);
			if (listDisplayAmount < 10 || listDisplayAmount > 50) {
				listDisplayAmount = 10;
			}
		}
		if (colIndex != null) {
			column = Integer.parseInt(colIndex);
			if (column < 0 || column > 5)
				column = 0;
		}
		if (sortDirection != null) {
			if (!sortDirection.equals("asc"))
				dir = "desc";
		}

		String colName = columnNames[column];
		int totalRecords = -1;
		try {
			totalRecords = getTotalRecordCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		RECORD_SIZE = listDisplayAmount;
//		GLOBAL_SEARCH_TERM = request.getSSearch();
		ID_SEARCH_TERM = request.getSSearch_0();
		NAME_SEARCH_TERM = request.getSSearch_1();
		PLACE_SEARCH_TERM = request.getSSearch_2();
		CITY_SEARCH_TERM = request.getSSearch_3();
		STATE_SEARCH_TERM = request.getSSearch_4();
		PHONE_SEARCH_TERM = request.getSSearch_5();
		COLUMN_NAME = colName;
		DIRECTION = dir;
		INITIAL = start;

		try {
			jsonResult = getPersonDetails(totalRecords);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResult;

	}

	public JsonObjectDto getPersonDetails(int totalRecords) {

		int totalAfterSearch = totalRecords;
		JsonObjectDto result = new JsonObjectDto();
		List<DataArrayDto> array = new ArrayList<>();
		String searchSQL = "";

		String sql = "SELECT id, name, place, city, state, phone FROM person WHERE ";

//		String globeSearch = "id LIKE '%" + GLOBAL_SEARCH_TERM + "%' OR name LIKE '%" + GLOBAL_SEARCH_TERM + "%' "
//				+ "OR place LIKE '%" + GLOBAL_SEARCH_TERM + "%' OR city LIKE '%" + GLOBAL_SEARCH_TERM + "%' "
//				+ "OR state LIKE '%" + GLOBAL_SEARCH_TERM + "%' OR phone LIKE '%" + GLOBAL_SEARCH_TERM + "%'";

		String idSearch = "id LIKE '%" + ID_SEARCH_TERM + "%'";
		String nameSearch = "name LIKE '%" + NAME_SEARCH_TERM + "%'";
		String placeSearch = "place LIKE '%" + PLACE_SEARCH_TERM + "%'";
		String citySearch = "city LIKE '%" + CITY_SEARCH_TERM + "%'";
		String stateSearch = "state LIKE '%" + STATE_SEARCH_TERM + "%'";
		String phoneSearch = "phone LIKE '%" + PHONE_SEARCH_TERM + "%'";

//		if (!GLOBAL_SEARCH_TERM.isEmpty()) {
//			searchSQL = globeSearch;
//		} else
//		if (!ID_SEARCH_TERM.isEmpty()) {
//			searchSQL = idSearch;
//		} else if (!NAME_SEARCH_TERM.isEmpty()) {
//			searchSQL = nameSearch;
//		} else if (!PLACE_SEARCH_TERM.isEmpty()) {
//			searchSQL = placeSearch;
//		} else if (!CITY_SEARCH_TERM.isEmpty()) {
//			searchSQL = citySearch;
//		} else if (!STATE_SEARCH_TERM.isEmpty()) {
//			searchSQL = stateSearch;
//		} else if (!PHONE_SEARCH_TERM.isEmpty()) {
//			searchSQL = phoneSearch;
//		}
		
		if (!ID_SEARCH_TERM.isEmpty()) {
			if(searchSQL.trim().endsWith("%'")) { searchSQL += " and "; }
			searchSQL += idSearch;
		} 
		if (!NAME_SEARCH_TERM.isEmpty()) {
			if(searchSQL.trim().endsWith("%'")) { searchSQL += " and "; }
			searchSQL += nameSearch;
		}
		if (!PLACE_SEARCH_TERM.isEmpty()) {
			if(searchSQL.trim().endsWith("%'")) { searchSQL += " and "; }
			searchSQL += placeSearch;
		}
		if (!CITY_SEARCH_TERM.isEmpty()) {
			if(searchSQL.trim().endsWith("%'")) { searchSQL += " and "; }
			searchSQL += citySearch;
		}
		if (!STATE_SEARCH_TERM.isEmpty()) {
			if(searchSQL.trim().endsWith("%'")) { searchSQL += " and "; }
			searchSQL += stateSearch;
		}
		if (!PHONE_SEARCH_TERM.isEmpty()) {
			if(searchSQL.trim().endsWith("%'")) { searchSQL += " and "; }
			searchSQL += phoneSearch;
		}

		sql += searchSQL;
		if (sql.trim().endsWith("WHERE")) {
			sql += " 1=1 ";
		}
		sql += " ORDER BY " + COLUMN_NAME + " " + DIRECTION;
		sql += " LIMIT " + INITIAL + ", " + RECORD_SIZE;

		System.out.println(sql);

		Query query = entityManager.createNativeQuery(sql);
		List<Object[]> results = query.getResultList();

		for (Object[] row : results) {
			DataArrayDto dataArrayDto = new DataArrayDto();
			dataArrayDto.setId(row[0].toString());
			dataArrayDto.setName(row[1].toString());
			dataArrayDto.setPlace(row[2].toString());
			dataArrayDto.setCity(row[3].toString());
			dataArrayDto.setState(row[4].toString());
			dataArrayDto.setPhone(row[5].toString());
			array.add(dataArrayDto);
		}

		if (!ID_SEARCH_TERM.isEmpty() || !NAME_SEARCH_TERM.isEmpty() || !PLACE_SEARCH_TERM.isEmpty()
				|| !CITY_SEARCH_TERM.isEmpty() || !STATE_SEARCH_TERM.isEmpty() || !PHONE_SEARCH_TERM.isEmpty()) {
//			String countQuery = "SELECT COUNT(*) FROM person WHERE " + searchSQL;
//			Query countQuery = entityManager.createNativeQuery(countQuery);
//			totalAfterSearch = Integer.parseInt(countQuery.getSingleResult().toString());
			String countQueryString = "SELECT COUNT(*) FROM person WHERE " + searchSQL;
			Query countQuery = entityManager.createNativeQuery(countQueryString);
			totalAfterSearch = Integer.parseInt(countQuery.getSingleResult().toString());
		}

		result.setITotalRecords(totalRecords);
		result.setITotalDisplayRecords(totalAfterSearch);
		result.setDataArray(array);

		return result;
	}

//	public JsonObjectDto getPersonDetails(int totalRecords) {
//
//		int totalAfterSearch = totalRecords;
//		JsonObjectDto result = new JsonObjectDto();
//		List<DataArrayDto> array = new ArrayList<>();
//		String searchSQL = "";
//
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		String dbConnectionURL = "jdbc:mysql://localhost:3306/faruk?user=root&password=root";
//		Connection con = DriverManager.getConnection(dbConnectionURL);
//		String sql = "SELECT " + "id, name, place, city, state, " + "phone " + "FROM " + "person " + "WHERE ";
//
//		String globeSearch = "id like '%" + GLOBAL_SEARCH_TERM + "%'" + "or name like '%" + GLOBAL_SEARCH_TERM + "%'"
//				+ "or place like '%" + GLOBAL_SEARCH_TERM + "%'" + "or city like '%" + GLOBAL_SEARCH_TERM + "%'"
//				+ "or state like  '%" + GLOBAL_SEARCH_TERM + "%'" + "or phone like '%" + GLOBAL_SEARCH_TERM + "%'";
//
//		String idSearch = "id like " + ID_SEARCH_TERM + "";
//		String nameSearch = "name like '%" + NAME_SEARCH_TERM + "%'";
//		String placeSearch = " place like '%" + PLACE_SEARCH_TERM + "%'";
//		String citySearch = " city like '%" + CITY_SEARCH_TERM + "%'";
//		String stateSearch = " state like '%" + STATE_SEARCH_TERM + "%'";
//		String phoneSearch = " phone like '%" + PHONE_SEARCH_TERM + "%'";
//		System.out.println(phoneSearch);
//		if (GLOBAL_SEARCH_TERM != "") {
//			searchSQL = globeSearch;
//		} else if (ID_SEARCH_TERM != "") {
//			searchSQL = idSearch;
//		} else if (NAME_SEARCH_TERM != "") {
//			searchSQL = nameSearch;
//		} else if (PLACE_SEARCH_TERM != "") {
//			searchSQL = placeSearch;
//		} else if (CITY_SEARCH_TERM != "") {
//			searchSQL = citySearch;
//		} else if (STATE_SEARCH_TERM != "") {
//			searchSQL = stateSearch;
//		} else if (PHONE_SEARCH_TERM != null) {
//			searchSQL = phoneSearch;
//
//		}
//
//		sql += searchSQL;
//		sql += " order by " + COLUMN_NAME + " " + DIRECTION;
//		sql += " limit " + INITIAL + ", " + RECORD_SIZE;
//
//		// for searching
//		PreparedStatement stmt = con.prepareStatement(sql);
//		ResultSet rs = stmt.executeQuery();
//
//		while (rs.next()) {
//			DataArrayDto ja = new DataArrayDto();
//			ja.setId(rs.getString("id"));
//			ja.setName(rs.getString("name"));
//			ja.setPlace(rs.getString("place"));
//			ja.setCity(rs.getString("city"));
//			ja.setState(rs.getString("state"));
//			ja.setPhone(rs.getString("phone"));
//			array.add(ja);
//			stmt.close();
//			rs.close();
//
//			String query = "SELECT " + "COUNT(*) as count " + "FROM " + "person " + "WHERE ";
//
//			// for pagination
//			if (GLOBAL_SEARCH_TERM != "" || ID_SEARCH_TERM != "" || NAME_SEARCH_TERM != "" || PLACE_SEARCH_TERM != ""
//					|| CITY_SEARCH_TERM != "" || STATE_SEARCH_TERM != "" || PHONE_SEARCH_TERM != "") {
//				query += searchSQL;
//
//				PreparedStatement st = con.prepareStatement(query);
//				ResultSet results = st.executeQuery();
//
//				if (results.next()) {
//					totalAfterSearch = results.getInt("count");
//				}
//				st.close();
//				results.close();
//				con.close();
//			}
//			try {
////   result.put("iTotalRecords", totalRecords);
////   result.put("iTotalDisplayRecords", totalAfterSearch);
////   result.put("aaData", array);
//				result.setITotalRecords(totalRecords);
//				result.setITotalDisplayRecords(totalAfterSearch);
//				result.setDataArray(array);
//			} catch (Exception e) {
//
//			}
//
//			return result;
//		}
//	}

	public int getTotalRecordCount() throws SQLException {
		return (int) personRepository.count();
	}

//	public int getTotalRecordCount() throws SQLException {
//
//		int totalRecords = -1;
//		String sql = "SELECT " + "COUNT(*) as count " + "FROM " + "person";
//		String dbConnectionURL = "jdbc:mysql://localhost:3306/myDB?user=root&password=root";
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		Connection con = DriverManager.getConnection(dbConnectionURL);
//
//		PreparedStatement statement = con.prepareStatement(sql);
//		ResultSet resultSet = statement.executeQuery();
//
//		if (resultSet.next()) {
//			totalRecords = resultSet.getInt("count");
//		}
//		resultSet.close();
//		statement.close();
//		con.close();
//
//		return totalRecords;
//	}

}