package com.sp.aop_demo.jquery;

import java.util.List;

import lombok.Data;

@Data
public class JsonObjectDto {
   private int iTotalRecords;
   private int iTotalDisplayRecords;
   private List<DataArrayDto> dataArray;

}