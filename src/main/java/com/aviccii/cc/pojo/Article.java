package com.aviccii.cc.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "tb_article")
public class Article {

  @Id
  private String id;
  	@Column(name = "user_id")
  private String user_id;
  	@Column(name = "category_id")
  private String category_id;
  	@Column(name = "content")
  private String content;
  	@Column(name = "type")
  private long type;
  	@Column(name = "status")
  private long status;
  	@Column(name = "view_count")
  private long view_count;
  	@Column(name = "publish_time")
  private java.sql.Timestamp publish_time;
  	@Column(name = "update_time")
  private java.sql.Timestamp update_time;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }


  public String getCategory_id() {
    return category_id;
  }

  public void setCategory_id(String category_id) {
    this.category_id = category_id;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public long getType() {
    return type;
  }

  public void setType(long type) {
    this.type = type;
  }


  public long getStatus() {
    return status;
  }

  public void setStatus(long status) {
    this.status = status;
  }


  public long getView_count() {
    return view_count;
  }

  public void setView_count(long view_count) {
    this.view_count = view_count;
  }


  public java.sql.Timestamp getPublish_time() {
    return publish_time;
  }

  public void setPublish_time(java.sql.Timestamp publish_time) {
    this.publish_time = publish_time;
  }


  public java.sql.Timestamp getUpdate_time() {
    return update_time;
  }

  public void setUpdate_time(java.sql.Timestamp update_time) {
    this.update_time = update_time;
  }

}
