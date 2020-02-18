package com.company.salesreport.job.fieldmapper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class EntryFieldSetMapper implements FieldSetMapper<Map<String, String>> {
	
	public static final String LINE_TYPE_CODE = "line_code";
	public static final String COL1 = "column1";
	public static final String COL2 = "column2";
	public static final String COL3 = "column3";
	public static final String SALESMAN_CODE = "001";
	public static final String SALE_CODE = "003";
	public static final String CLIENT_CODE = "002";

	@Override
	public Map<String, String> mapFieldSet(FieldSet fieldSet) throws BindException {

		final Map<String, String> map = new HashMap<>();
		final String code = fieldSet.readString(EntryFieldSetMapper.LINE_TYPE_CODE);
		map.put(EntryFieldSetMapper.LINE_TYPE_CODE, code);
		
		if (code.equals(EntryFieldSetMapper.SALE_CODE)) {
			map.put(EntryFieldSetMapper.COL1, fieldSet.readString(EntryFieldSetMapper.COL1));
			map.put(EntryFieldSetMapper.COL2, fieldSet.readString(EntryFieldSetMapper.COL2));
			map.put(EntryFieldSetMapper.COL3, fieldSet.readString(EntryFieldSetMapper.COL3));
		}

		return map;
	}
}
