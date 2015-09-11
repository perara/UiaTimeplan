package no.persoft.uia.timeplan.Fetcher;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import no.persoft.uia.timeplan.model.Course;
import no.persoft.uia.timeplan.model.CourseItem;
import no.persoft.uia.timeplan.model.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PerArne on 08.07.2014.
 */
public class Fetcher {
    private boolean isSubject;

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Fetcher.class.getName());

    public Fetcher(boolean isSubject){
        this.isSubject = isSubject;
    }

    public boolean run() throws IOException {
        System.out.println("Starting...");

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        HtmlPage page = (HtmlPage)webClient.getPage("http://timeplan.uia.no/swsuiah/restrict/no/default.aspx");

        if(this.isSubject) {
            HtmlAnchor anch = (HtmlAnchor) page.getElementById("LinkBtn_modules");
            page = anch.click();
        }

        //page = (HtmlPage)page.getAnchorByHref("javascript:__doPostBack('LinkBtn_modules','')").click();
        //page = (HtmlPage)page.getAnchorByHref("javascript:__doPostBack('LinkBtn_studentsets','')").click();

        //HtmlRadioButtonInput rButton2 = (HtmlRadioButtonInput)page.getElementById("RadioType_0");
        //rButton2.setChecked(true);
        //rButton2.click();

        HtmlSelect form = (HtmlSelect)page.getElementById("dlObject");
        HtmlSubmitInput submit = (HtmlSubmitInput)page.getElementByName("bGetTimetable");

        // Create a array which store courseNames (Filter duplicates)
        ArrayList<String> courseNames = new ArrayList<String>();

        List<HtmlElement> optionList = form.getElementsByTagName("option");
        int count = 0;
        for (HtmlElement options : optionList)
        {
            count++;
            HtmlOption option = form.getOptionByValue(options.getAttribute("value"));
            form.setSelectedAttribute(option, true);
            HtmlPage coursePage = (HtmlPage)submit.click();
            form.setSelectedAttribute(option, false);

            String courseName = options.getTextContent();
            String courseHTML = coursePage.getWebResponse().getContentAsString();

            // Add a suffix to make it unique (Checks if the name already exists) // TODO - Should report incidence
            while(courseNames.contains(courseName))
            {
                courseName += ".";
            }
            courseNames.add(courseName);

            log.info("Processing " + courseName + " | " + count + "/" + optionList.size());
            this.processCourse(courseName, courseHTML);
        }


        return true;
    }

    private boolean processCourse(String courseName, String courseHtml)
    {
        Document doc = Jsoup.parse(courseHtml);

        Session session = EntityManagerFactory.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Course course = new Course();
        course.setName(courseName);
        course.setIsSubject(this.isSubject ? true : false);

        // Check if already exists
        Course doesCourseExist = (Course)session.createCriteria(Course.class)
                .add(Restrictions.eq("name", course.getName()))
                .uniqueResult();

        if(doesCourseExist == null) {
            course.setLastUpdate(new Timestamp(new Date().getTime()).toString());
            session.saveOrUpdate(course);
        }
        else {
            System.out.println(course.getName() + " already exists... Dump DB and continue");
            course = doesCourseExist;
            course.setLastUpdate(new Timestamp(new Date().getTime()).toString());
            session.update(course);

            // Delete all course items (We are gonna update it
            session.createQuery("delete from CourseItem  as ci where ci.course = " + course.getId()).executeUpdate();
        }


        int count = 0;
        for (Element tr : doc.getElementsByClass("tr2"))
        {
            Pattern pattern = Pattern.compile("\\d{2}(?:\\d{2})?");
            Matcher matcher = pattern.matcher(tr.parent().getElementsByClass("td1").text());
            if (matcher.find())
            {
                String weekAndYear = tr.parent().getElementsByClass("td1").text();
                Integer year = Integer.parseInt(weekAndYear.split(",")[1].trim()); // IE: 2015
                Integer week = Integer.parseInt(weekAndYear.split(",")[0].replaceAll("[a-zA-Z ]", ""));

                String day = tr.children().get(0).text(); // IE Man ons tors
                String date  =  tr.children().get(1).text(); // IE: 26 Nov
                String time =  tr.children().get(2).text(); // IE: 08.15-16.00
                String startTime = time.split("-")[0]; // IE: 08.15
                String endTime = time.split("-")[1]; // IE : 16.00
                String subjectName = tr.children().get(3).text(); // IE: KKK100-G/Forelesning/03 Forming //TODO -usage?
                String location = tr.children().get(4).text(); // IE : CG-C3 084 lab-design/tegning
                String lecturer = tr.children().get(5).text(); // IE : Myran, Turid

                Date fromDate = Helpers.DateConvert(year, week, day, startTime);
                Date toDate = Helpers.DateConvert(year, week, day, endTime);

                CourseItem courseItem = new CourseItem();
                courseItem.setCourse(course);
                courseItem.setFromDate(new Timestamp(fromDate.getTime()));
                courseItem.setToDate(new Timestamp(toDate.getTime()));
                courseItem.setEducator(lecturer);
                courseItem.setRoom(location);
                courseItem.setActivity(subjectName);

                session.saveOrUpdate(courseItem);
                session.flush();
            }
        }

        tx.commit();
        session.close();

        return true;
    }

}
