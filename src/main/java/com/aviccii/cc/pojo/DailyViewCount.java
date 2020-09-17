package com.aviccii.cc.pojo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "tb_daily_view_count")
public class DailyViewCount {

  @Id
  private String id;
  	@Column(name = "view_count")
  private long view_count;
  	@Column(name = "create_time")
  private String create_time;
  	@Column(name = "update_time")
  private String update_time;


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


  public String getCreate_time() {
    return create_time;
  }

  public void setCreate_time(String create_time) {
    this.create_time = create_time;
  }


  public String getUpdate_time() {
    return update_time;
  }

  public void setUpdate_time(String update_time) {
    this.update_time = update_time;
  }

}
