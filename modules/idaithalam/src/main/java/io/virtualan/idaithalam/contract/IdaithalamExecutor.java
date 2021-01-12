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

package io.virtualan.idaithalam.contract;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import io.cucumber.core.cli.Main;
import io.virtualan.cucumblan.props.ApplicationConfiguration;
import io.virtualan.idaithalam.core.UnableToProcessException;
import io.virtualan.idaithalam.core.contract.validator.FeatureFileGenerator;
import io.virtualan.idaithalam.core.domain.Item;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
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
 * Entry point to generate feature file and cucumber report and execute and generate for VIRTUALAN
 * or POSTMAN collection and used for API contract testing capability.
 * <p>
 * it will generate the feature file for VIRTUALAN collection and POSTMAN collection
 * <p>
 * - Used for API testing. - Used for Contract testing. - Used for Production Checkout. - Used for
 * Agile sprint end regression testing.
 */
public class IdaithalamExecutor {
    private final static Logger LOGGER = Logger.getLogger(IdaithalamExecutor.class.getName());

    /**
     * The Feature.
     */
    static String feature = "Idaithalam";

    /**
     * Instantiates a new Idaithalam executor.
     *
     * @throws UnableToProcessException the unable to process exception
     */
    public IdaithalamExecutor() throws UnableToProcessException {

    }

    /**
     * Entry point
     *
     * @param args the input arguments
     * @throws UnableToProcessException the unable to process exception
     */
    public static void main(String[] args) throws UnableToProcessException {
        String feature = "Idaithalam";
        if (args.length > 0) {
            feature = args[0];
        }
        validateContract(feature);
    }

    /**
     * Validate contract.
     *
     * @param featureHeading the feature heading
     * @return the int
     * @throws UnableToProcessException the unable to process exception
     */
    public static int validateContract(String featureHeading)
        throws UnableToProcessException {
        byte exitStatus;
        try {
            feature = featureHeading;
            generateFeatureFile();
            addConfToClasspath();
            String[] argv = getCucumberOptions();
            exitStatus = Main.run(argv, Thread.currentThread().getContextClassLoader());
            generateReport();
        } catch (IOException | UnableToProcessException e) {
            LOGGER.severe("Provide appropriate input data? : " + e.getMessage());
            throw new UnableToProcessException("Provide appropriate input data? : " + e.getMessage());
        }
        return exitStatus;
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
        reportBuilder.generateReports();
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
                "--glue", "io.virtualan.cucumblan.core", "", "conf/feature/",
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
     * @throws IOException
     */

    private static void generateFeatureFile() throws IOException, UnableToProcessException {
        List<List<Item>> items = FeatureFileGenerator.generateFeatureFile();
        String okta = ApplicationConfiguration.getProperty("service.api.okta");
        String featureTitle = ApplicationConfiguration.getProperty("virtualan.data.heading");
        for(int i=0; i< items.size(); i++){
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile("virtualan-contract.mustache");
            if(!new File("conf").exists()){
                new File("conf").mkdir();
            }
            if( !new File("conf/feature").exists()){
                new File("conf/feature").mkdir();
            }
            FileOutputStream outputStream = new FileOutputStream("conf/feature/virtualan-contract."+i+".feature");
            Writer writer = new OutputStreamWriter(outputStream);
            mustache.execute(writer, new FeatureFileMapping(getTitle(featureTitle != null ? featureTitle.split(";") : feature.split(":"), i, feature), items.get(i),okta)).flush();
            writer.close();
        }
    }

    private static String getTitle(String[] arrayTitle, int index , String defaultString){
        try{
            return arrayTitle[index];
        }catch (Exception e){
            return defaultString + "_"+index;
        }
    }
}
