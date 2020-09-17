package com.aviccii.cc.pojo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "tb_labels")
public class Labels {

  @Id
  private String id;
  	@Column(name = "name")
  private String name;
  	@Column(name = "count")
  private long count;
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


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
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
