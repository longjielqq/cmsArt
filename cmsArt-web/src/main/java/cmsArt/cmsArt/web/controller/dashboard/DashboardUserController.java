package cmsArt.cmsArt.web.controller.dashboard;

import cmsArt.cmsArt.biz.service.DictionaryService;
import cmsArt.cmsArt.biz.service.UserService;
import cmsArt.cmsArt.model.vo.ShiroUser;
import cmsArt.cmsArt.model.vo.User;
import cmsArt.cmsArt.model.vo.UserProfile;
import cmsArt.cmsArt.web.controller.BaseController;
import cmsArt.cmsArt.web.util.FileUpload;
import cmsArt.cmsArt.web.util.Images;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author kangyonggan
 * @since 2017/1/20
 */
@Controller
@RequestMapping("dashboard/user")
public class DashboardUserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 基本信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "profile", method = RequestMethod.GET)
    @RequiresPermissions("USER_PROFILE")
    public String profile(Model model) {
        ShiroUser shiroUser = userService.getShiroUser();
        User user = userService.findUserById(shiroUser.getId());
        UserProfile userProfile = userService.findUserProfileByUsername(shiroUser.getUsername());

        model.addAttribute("user", user);
        model.addAttribute("userProfile", userProfile);
        return getPathRoot() + "/profile";
    }

    @RequestMapping(value = "profile", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> profile(@ModelAttribute(value = "userProfile") @Valid UserProfile userProfile, BindingResult result,
                                       @ModelAttribute(value = "user") @Valid User user, BindingResult bindingResult,
                                       @RequestParam(value = "avatar", required = false) MultipartFile avatar) throws FileUploadException {
        Map<String, Object> resultMap = getResultMap();
        ShiroUser shiroUser = userService.getShiroUser();

        if (!result.hasErrors() && !bindingResult.hasErrors()) {
            if (avatar != null && !avatar.isEmpty()) {
                String fileName = FileUpload.upload(avatar);
                String large = Images.large(fileName);
                userProfile.setLargeAvatar(large);
                String middle = Images.middle(fileName);
                userProfile.setMediumAvatar(middle);
                String small = Images.small(fileName);
                user.setSmallAvatar(small);
            }

            userService.updateUserAndProfile(user, userProfile);
            resultMap.put("user", userService.findUserById(shiroUser.getId()));
            resultMap.put("userProfile", userService.findUserProfileByUsername(shiroUser.getUsername()));
        } else {
            setResultMapFailure(resultMap);
        }

        return resultMap;
    }

}
