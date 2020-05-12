package HMX.Service;

import HMX.mapper.ArticleMapper;
import HMX.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    public  List<Article> queryArticles() {
        return articleMapper.selectAll();
    }

    public Article queryArticle(Long id) {
        return articleMapper.selectByPrimaryKey(id);
    }

    public List<Article> queryArticlesByUserId(Long id) {
        return articleMapper.queryArticlesByUserId(id);
    }

    public int updateByCondtion(Article article) {
        return articleMapper.updateByCondtion(article);
    }

    public int insert(Article article) {
        return articleMapper.insert(article);
    }
}
