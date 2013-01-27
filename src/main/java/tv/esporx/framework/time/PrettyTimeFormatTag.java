package tv.esporx.framework.time;

import com.ocpsoft.pretty.time.PrettyTime;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import tv.esporx.framework.mvc.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Locale;

import static org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext;

public class PrettyTimeFormatTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrettyTimeFormatTag.class);

    private DateTime date;
    private Locale locale;
    private JspContext jspContext;


    public void setDate(DateTime date) {
        this.date = date;
    }

    @Override
    public void setJspContext(JspContext jspContext) {
        this.jspContext = jspContext;
        PageContext context = (PageContext) jspContext;
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        RequestUtils requestUtils = getWebApplicationContext(context.getServletContext()).getBean(RequestUtils.class);
        locale = new Locale(requestUtils.currentLocale(request));

    }

    @Override
    public void doTag() throws JspException, IOException {
        PageContext pageContext = (PageContext) jspContext;
        try {
            JspWriter out = pageContext.getOut();
            out.println(new PrettyTime(locale).format(date.toDate()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
