package com.aviccii.cc.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "tb_kpi_daily")
public class KpiDaily {

  @Id
  private String id;
  	@Column(name = "view_count")
  private long view_count;
  	@Column(name = "update_time")
  private java.sql.Timestamp update_time;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public long getView_count() {
    return view_count;
  }

  public void setView_count(long view_count) {
    this.view_count = view_count;
  }


  public java.sql.Timestamp getUpdate_time() {
    return update_time;
  }

  public void setUpdate_time(java.sql.Timestamp update_time) {
    this.update_time = update_time;
  }

}
