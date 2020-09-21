package com.aviccii.cc.controller.user;

import com.aviccii.cc.pojo.User;
import com.aviccii.cc.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author aviccii 2020/9/17
 * @Discrimination
 */

@Slf4j
@RestController
@RequestMapping("/user")
public class UserApi {

    @PostMapping("/admin_account")
    public ResponseResult initManagerAccount(@RequestBody User user) {
        log.info("user name ==========>" + user.getUser_name());
        log.info("user password ==========>" + user.getPassword());
        log.info("user email ==========>" + user.getEmail());
        return ResponseResult.SUCCESS();
    }

    @PostMapping
    public ResponseResult register(@RequestBody User user) {
        return null;
    }

    @PostMapping("/{captcha}")
    public ResponseResult login(@PathVariable("captcha") String captcha, @RequestBody User user) {
        return null;
    }

    @GetMapping("/capthcha")
    public ResponseResult getCaptcha() {
        return null;
    }

    /**
     * 在处理方法入参处使用 @RequestParam 可以把请求参数传递给请求方法
     * @param emailAddress
     * @return
     */
    @GetMapping("/getVerifyCode")
    public ResponseResult sendVerifyCode(@RequestParam("email")String emailAddress){
        log.info("email ==> " +emailAddress);
        return ResponseResult.SUCCESS();
    }

    /**
     *@ PostMapping 和 @PutMapping 作用等同，都是用来向服务器提交信息。
     * 如果是添加信息，倾向于用@PostMapping，如果是更新信息，倾向于用@PutMapping。两者差别不是很明显。
     * @param user
     * @return
     */
    @PutMapping("/password/{userId}")
    public ResponseResult updatePassword(@PathVariable("userId")String userId,@RequestBody User user){
        return null;
    }

    /**
     * 使用@PathVariable可以快速的访问URL中的部分内容。
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseResult getUserInfo(@PathVariable("userId") String userId){
        return null;
    }

    @PutMapping("/{userId}")
    public ResponseResult updateUserInfo(@PathVariable("userId")String userId,@RequestBody User user){
        return null;
    }

    @GetMapping("/list")
    public ResponseResult listUsers(@RequestParam("page")int page,@RequestParam("size")int size){
        return null;
    }

    @DeleteMapping("/{userId}")
    public ResponseResult deleteUser(@PathVariable("userId")String userId){
        return null;
    }

}
