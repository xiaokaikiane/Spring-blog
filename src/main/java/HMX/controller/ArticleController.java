package HMX.controller;

import HMX.Service.ArticleService;
import HMX.Service.CategoryService;
import HMX.Service.CommentService;
import HMX.model.Article;
import HMX.model.Category;
import HMX.model.Comment;
import HMX.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private  CommentService commentService;
    @Autowired
    private CategoryService categoryService;
    @RequestMapping("/")
    public String index(Model model){
        //用户登录以后user对象要从session获取,并设置到页面需要的属性中
        List<Article> articles= articleService.queryArticles();
        model.addAttribute("articleList",articles);
        return "index";
    }
    @RequestMapping("/a/{id}")
    public String detail(@PathVariable("id") Long id,Model model){
        Article article=articleService.queryArticle(id);
        List<Comment> comments=commentService.queryComments(id);
        article.setCommentList(comments);
        model.addAttribute("article",article);
        return "info";
    }
    @RequestMapping("/writer")
    public String writer(HttpSession session,Long activeCid,Model model){
        User user=(User)session.getAttribute("user");
        List<Article> articles=articleService.queryArticlesByUserId(user.getId());
        model.addAttribute("articleList",articles);
        List<Category> categories=categoryService.queryCategoriesByUserId(user.getId());
        model.addAttribute("categoryList",categories);
        model.addAttribute("activeCid",activeCid==null?
                categories.get(0).getId():activeCid);
        return "writer";
    }

    /**
     * 跳转到新增文章/修改页面(同一个页面editor)
     * @param type 新增为1,修改为2
     * @param id   新增为categoryId,修改为articleId
     * @param model editor页面需要type类型,都需要category,新增需要activeCid,修改时需要article
     * @return
     */
    @RequestMapping("/writer/forward/{type}/{id}/editor")
    public String editor(@PathVariable("type") Integer type,
                            @PathVariable("id") Long id, Model model){
        Category category;
        if(type==1){//1,新增
            category=categoryService.querycategoryById(id);
            model.addAttribute("activeCid",id);
        }else{//修改
            Article article=articleService.queryArticle(id);
            model.addAttribute("article",article);
            category=categoryService.querycategoryById(new Long(article.getCategoryId()));
        }
        model.addAttribute("type",type);
        model.addAttribute("category",category);
        return "editor";
    }
    @RequestMapping(value = "/writer/article/{type}/{id}",method = RequestMethod.POST)
    public String publish(@PathVariable("type") Integer type,
    @PathVariable("id") Integer id,Article article,Model model,HttpSession session){
        article.setUpdatedAt(new Date());
        if(type==1){//新增插入数据
            article.setCategoryId(id);
            User user=(User)session.getAttribute("user");
            article.setUserId(user.getId());
            article.setCoverImage("https://picsum.photos/id/1/400/300");
            article.setCreatedAt(new Date());
            article.setStatus((byte)0);
            article.setViewCount(0L);
            article.setCommentCount(0);
            int num=articleService.insert(article);
            id=article.getId().intValue();
        }else{//修改时修改文章
            article.setId(new Long(id));
            int num=articleService.updateByCondtion(article);
        }
        return String.format("redirect:/writer/forward/2/%s/editor",id);
    }
}
