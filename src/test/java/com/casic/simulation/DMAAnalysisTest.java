package com.casic.simulation;

import com.casic.simulation.dma.DMAAnalysis;
import com.casic.simulation.dma.DMAResult;
import com.casic.simulation.dma.dmamanager.MinimumFlowJob;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2017/3/24.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:DMAAnalysisTest-context.xml"
})
@TestExecutionListeners(
        {DependencyInjectionTestExecutionListener.class,
                DbUnitTestExecutionListener.class})
@DbUnitConfiguration(databaseConnection = "dbUnitDatabaseConnection")
public class DMAAnalysisTest {

    @Resource
    public DataSource dataSource;

    @Resource
    public DMAAnalysis dmaAnalysis;

    @Resource
    public MinimumFlowJob minimumFlowJob;

 /*   @BeforeClass
    public static void setUp() throws Exception {
        IDatabaseConnection iconn = null;
        IDatabaseTester databaseTester;
        databaseTester = new JdbcDatabaseTester("oracle.jdbc.driver.OracleDriver",
                "jdbc:oracle:thin:@192.168.0.203:1521:NBDB", "ningbo", "ningbo");

        iconn = databaseTester.getConnection();
        QueryDataSet dataSet = new QueryDataSet(iconn);
        dataSet.addTable("DMAINFO", "select * from " + "DMAINFO");
        dataSet.addTable("ALARM_DMA_SALE_WATER", "select * from " + "ALARM_DMA_SALE_WATER");
        dataSet.addTable("POSITIONINFO", "select * from " + "POSITIONINFO");
        dataSet.addTable("POSDMA", "select * from " + "POSDMA");
        dataSet.addTable("DEVPOS", "select * from " + "DEVPOS");
        dataSet.addTable("ad_dj_press", "select * from " + "ad_dj_press");
        dataSet.addTable("ad_dj_flow", "select * from " + "ad_dj_flow");
        FlatXmlDataSet.write(dataSet, new FileOutputStream("data.xml"));
    }
*/

    @Test
/*
    @DatabaseSetup("../../../resources/data.xml")
*/
    public void getLeakageRateByRange() throws Exception {
        try {
            SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf0.parse("2016-11-28");

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Date end = sdf1.parse("2016-11-29");

            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            Date end2 = sdf1.parse("2016-11-30");

            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
            Date end3 = sdf1.parse("2016-11-31");

            minimumFlowJob.saveMinimumFlowByDay(end);
            minimumFlowJob.saveMinimumFlowByDay(end2);
            minimumFlowJob.saveMinimumFlowByDay(end3);

//            DMAResult dmaResult = dmaAnalysis.getLeakageRateByRange(start, end, 361);
//            System.out.println(dmaResult.getCode());
//            System.out.println(dmaResult.getErrorMsg());
//            System.out.println(dmaResult.getLeakageRate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}