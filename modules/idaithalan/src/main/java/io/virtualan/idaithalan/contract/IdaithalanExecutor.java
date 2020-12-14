/*
 *
 *  Copyright (c) 2020.  Virtualan Contributors (https://virtualan.io)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under the License
 *   is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *   or implied. See the License for the specific language governing permissions and limitations under
 *   the License.
 *
 */

package io.virtualan.idaithalan.contract;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import io.cucumber.core.cli.Main;
import io.virtualan.cucumblan.props.ApplicationConfiguration;
import io.virtualan.idaithalan.core.contract.validator.FeatureFileGenerator;
import io.virtualan.idaithalan.core.domain.Item;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Reportable;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.presentation.PresentationMode;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


/**
 *
 *  Entry point to generate feature file and cucumber report and execute and generate for VIRTUALAN or POSTMAN collection
 *  and used for API contract testing capability.
 *
 *  it will generate the feature file for VIRTUALAN collection and POSTMAN collection
 *
 *  - Used for API testing.
 *  - Used for Contract testing.
 *  - Used for Production Checkout.
 *  - Used for Agile sprint end regression testing.
 *
 */
public class IdaithalanExecutor {
    private final static Logger LOGGER = Logger.getLogger(IdaithalanExecutor.class.getName());

    static String feature = "Idithalan";
    List<Item> items = FeatureFileGenerator.generateFeatureFile();
    String okta = ApplicationConfiguration.getProperty("service.api.okta");

    /**
     * Entry point
     *
     * @param args
     */

    public static void main(String[] args)  {
        if (args.length > 0) {
            feature = args[0];
        }
        validateContract(feature);
    }

    public static void validateContract(String feature)  {
        try {
            generateFeatureFile(feature);
            addConfToClasspath();
            String[] argv = getCucumberOptions();
            byte exitStatus = Main.run(argv, Thread.currentThread().getContextClassLoader());
            generateReport();
        } catch (IOException e) {
            LOGGER.severe("Provide appropriate input data? : " + e.getMessage());
            System.exit(-1);
        }
    }

    /**
     *  generate cucumber report
     */

    private static void generateReport() {
        File reportOutputDirectory = new File("target");
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("target/cucumber.json");
        String buildNumber = "1";
        String projectName = feature + " - API Contract Testing";

        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        // optional configuration - check javadoc for details
        configuration.addPresentationModes(PresentationMode.RUN_WITH_JENKINS);
        // do not make scenario failed when step has status SKIPPED
        configuration.setNotFailingStatuses(Collections.singleton(Status.SKIPPED));
        configuration.setBuildNumber(buildNumber);
        // additional metadata presented on main page
        configuration.addClassifications("Platform", "Windows");
        configuration.addClassifications("Browser", "Firefox");
        configuration.addClassifications("Branch", "release/1.0");
        // optionally specify qualifiers for each of the report json files
        configuration.addPresentationModes(PresentationMode.PARALLEL_TESTING);
        configuration.setQualifier("cucumber-report-1", "First report");
        configuration.setQualifier("cucumber-report-2", "Second report");
        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        Reportable result = reportBuilder.generateReports();
    }

    /**
     * cucumber options
     *
     * @return
     */

    private static String[] getCucumberOptions() {
        return new String[]{
                "-p", "json:target/cucumber.json",
                "-p", "html:target/cucumber-html-report.html",
                "--glue", "io.virtualan.cucumblan.core", "", "conf/virtualan-contract.feature",
        };
    }

    /**
     * add conf to classpath
     *
     * @throws MalformedURLException
     */

    private static void addConfToClasspath() throws MalformedURLException {
        ClassLoader prevCl = Thread.currentThread().getContextClassLoader();
        ClassLoader urlCl = URLClassLoader
                .newInstance(new URL[]{new File("conf").toURI().toURL()}, prevCl);
        Thread.currentThread().setContextClassLoader(urlCl);
    }

    /**
     * Generate the feature file for the provided collection
     *
     * @param feature
     * @throws IOException
     */

    private static void generateFeatureFile(String feature) throws IOException {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("virtualan-contract.mustache");
        FileOutputStream outputStream = new FileOutputStream("conf/virtualan-contract.feature");
        Writer writer = new OutputStreamWriter(outputStream);
        mustache.execute(writer, new IdaithalanExecutor()).flush();
        writer.close();
    }
}
