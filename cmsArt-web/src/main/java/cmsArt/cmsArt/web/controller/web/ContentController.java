package cmsArt.cmsArt.web.controller.web;

import cmsArt.cmsArt.biz.service.AttachmentService;
import cmsArt.cmsArt.biz.service.ContentService;
import cmsArt.cmsArt.biz.util.MarkdownUtil;
import cmsArt.cmsArt.model.vo.Attachment;
import cmsArt.cmsArt.model.vo.Content;
import cmsArt.cmsArt.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2017/1/21
 */
@Controller
@RequestMapping("content")
public class ContentController extends BaseController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private AttachmentService attachmentService;

    /**
     * 内容详情
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "{id:[\\d]+}", method = RequestMethod.GET)
    public String detail(@PathVariable("id") Long id, Model model) {
        Content content = contentService.findContentById(id);
        content.setBody(MarkdownUtil.markdownToHtml(content.getBody()));
        List<Attachment> attachments = attachmentService.findAttachmentsBySourceIdAndType(id, "content");

        model.addAttribute("content", content);
        model.addAttribute("attachments", attachments);
        return getPathDetail();
    }

}
