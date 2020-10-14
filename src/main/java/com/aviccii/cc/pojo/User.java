package com.aviccii.cc.pojo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Table(name = "tb_user")
public class User {

  public User(){

  }

  public User(String id,String userName,String role,String avatar,String email,String sign,
              String state,String reg_ip,String login_ip,Date createTime,Date updateTime){
    this.id =id;
    this.userName =userName;
    this.role =role;
    this.avatar =avatar;
    this.email =email;
    this.sign =sign;
    this.state =state;
    this.reg_ip =reg_ip;
    this.login_ip =login_ip;
    this.createTime =createTime;
    this.updateTime =updateTime;
    this.password="";
  }

  @Id
  private String id;
  	@Column(name = "user_name")
  private String userName;
  	@Column(name = "password")
  private String password;
  @Column(name = "roles")
  private String role;
  	@Column(name = "avatar")
  private String avatar;
  	@Column(name = "email")
  private String email;
  	@Column(name = "sign")
  private String sign;
  	@Column(name = "state")
  private String state;
  	@Column(name = "reg_ip")
  private String reg_ip;
  	@Column(name = "login_ip")
  private String login_ip;
  	@Column(name = "create_time")
  private Date createTime;
  	@Column(name = "update_time")
  private Date updateTime;

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getUserName() {
    return userName;
  }

  public void setUserName(String user_name) {
    this.userName = user_name;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }


  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }


  public String getReg_ip() {
    return reg_ip;
  }

  public void setReg_ip(String reg_ip) {
    this.reg_ip = reg_ip;
  }


  public String getLogin_ip() {
    return login_ip;
  }

  public void setLogin_ip(String login_ip) {
    this.login_ip = login_ip;
  }


  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }
}
