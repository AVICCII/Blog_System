package com.aviccii.cc.pojo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "tb_categories")
public class Category {

  @Id
  private String id;
  	@Column(name = "category_name")
  private String category_name;
  	@Column(name = "category_py")
  private String category_py;
  	@Column(name = "description")
  private String description;
  	@Column(name = "`order`")
  private long order;
  	@Column(name = "status")
  private String status;
  	@Column(name = "create_time")
  private java.sql.Timestamp create_time;
  	@Column(name = "update_time")
  private java.sql.Timestamp update_time;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getCategory_name() {
    return category_name;
  }

  public void setCategory_name(String category_name) {
    this.category_name = category_name;
  }


  public String getCategory_py() {
    return category_py;
  }

  public void setCategory_py(String category_py) {
    this.category_py = category_py;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public long getOrder() {
    return order;
  }

  public void setOrder(long order) {
    this.order = order;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public java.sql.Timestamp getCreate_time() {
    return create_time;
  }

  public void setCreate_time(java.sql.Timestamp create_time) {
    this.create_time = create_time;
  }


  public java.sql.Timestamp getUpdate_time() {
    return update_time;
  }

  public void setUpdate_time(java.sql.Timestamp update_time) {
    this.update_time = update_time;
  }

}
