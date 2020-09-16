package com.aviccii.cc.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "tb_settings")
public class Settings {

  @Id
  private String id;
  	@Column(name = "setting_key")
  private String setting_key;
  	@Column(name = "setting_value")
  private String setting_value;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getSetting_key() {
    return setting_key;
  }

  public void setSetting_key(String setting_key) {
    this.setting_key = setting_key;
  }


  public String getSetting_value() {
    return setting_value;
  }

  public void setSetting_value(String setting_value) {
    this.setting_value = setting_value;
  }

}
