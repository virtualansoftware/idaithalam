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
import io.virtualan.idaithalam.config.IdaithalamConfiguration;
import io.virtualan.idaithalam.core.UnableToProcessException;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.idaithalam.core.domain.Item;
import io.virtualan.idaithalam.core.generator.FeatureFileGenerator;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.presentation.PresentationMode;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;


/**
 * Entry point to generate feature file and cucumber report and execute and generate for VIRTUALAN
 * or POSTMAN collection and used for API contract testing capability.
 * <p>
 * it will generate the feature file for VIRTUALAN collection and POSTMAN collection
 * <p>
 * - Used for API testing. - Used for Contract testing. - Used for Production Checkout. - Used for
 * Agile sprint end regression testing.
 * 
 * boolean isSuccess = VirtualanTestPlanExecutor.invoke("apiexecution.yaml");
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
     * Validate contract int.
     *
     * @param featureHeading the feature heading
     * @return the int
     * @throws UnableToProcessException the unable to process exception
     */
    public static int validateContract(String featureHeading)
        throws UnableToProcessException {
        return validateContract(featureHeading, null);
    }

    /**
     * Validate contract int.
     *
     * @param featureHeading   the feature heading
     * @param apiExecutorParam the api executor param
     * @return the int
     * @throws UnableToProcessException the unable to process exception
     */
    public static int validateContract(String featureHeading, ApiExecutorParam apiExecutorParam)
            throws UnableToProcessException {
        byte exitStatus;
        try {
            VirtualanClassLoader classLoaderParnet = new VirtualanClassLoader(IdaithalamExecutor.class.getClassLoader());
            ExecutionClassloader classLoader = addConfToClasspath(classLoaderParnet, apiExecutorParam.getOutputDir());
            /** @author Oliver Glas. */
            if (apiExecutorParam.getExecution() == null || apiExecutorParam.getExecution().equalsIgnoreCase("execute") == false) {
                generateFeatureFile(classLoader, apiExecutorParam);
            }
            if (apiExecutorParam.getExecution() != null && apiExecutorParam.getExecution().equalsIgnoreCase("generate")) {
                LOGGER.info("Test execution stopped after feature file generation due to parameter execution=generate.");
                return 0;
            }
            String fileIndex = UUID.randomUUID().toString();
            String[] argv = getCucumberOptions(apiExecutorParam, fileIndex);
            exitStatus = Main.run(argv, classLoader);
            if (IdaithalamConfiguration.isReportEnabled()) {
                generateReport(featureHeading, apiExecutorParam, fileIndex);
            }
        } catch (IOException | UnableToProcessException e) {
            LOGGER.severe("Provide appropriate input data? : " + e.getMessage());
            throw new UnableToProcessException("Provide appropriate input data? : " + e.getMessage());
        }
        return exitStatus;
    }

    private static String[] getCucumberOptions(ApiExecutorParam apiExecutorParam, String build) {
        String path = apiExecutorParam.getOutputDir();
        String jsonPath = path;
        if(apiExecutorParam.getOutputJsonDir() != null){
            if(new File(apiExecutorParam.getOutputJsonDir()).exists()){
                new File(apiExecutorParam.getOutputJsonDir()).mkdirs();
            }
            jsonPath = apiExecutorParam.getOutputJsonDir();
        }
        return new String[]{
            "-p", "pretty",
            "-p", "io.virtualan.cucumblan.props.hook.FeatureScope",
            "-p", path == null ? "json:target/cucumber-"+build+".json" : "json:"+jsonPath+"/cucumber-"+build+".json",
            "-p", path == null ? "html:target/cucumber-html-report.html" : "html:"+jsonPath+"/cucumber-html-report.html",
            "--glue", "io.virtualan.cucumblan.core", "", path == null ? "conf/feature/" : path+"/feature/",
        };
    }



    /**
     *  generate cucumber report
     */

    private static void generateReport(String featureHeading, ApiExecutorParam apiExecutorParam, String index) {
        String path = apiExecutorParam.getOutputDir();
        if(apiExecutorParam.getOutputJsonDir() != null){
            path = apiExecutorParam.getOutputJsonDir();
        }
        path = path == null ? "target" : path;
        File reportOutputDirectory = new File(path);
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add(path+"/cucumber-"+index+".json");
        String buildNumber = index;
        String projectName = featureHeading;
        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        configuration.setNotFailingStatuses(Collections.singleton(Status.SKIPPED));
        configuration.setBuildNumber(buildNumber);
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

    private static String[] getCucumberOptions(String path) {
        return new String[]{
            "-p", path == null ? "json:target/cucumber.json" : "json:"+path+"/cucumber.json",
            "-p", path == null ? "html:target/cucumber-html-report.html" : "html:"+path+"/cucumber-html-report.html",
            "--glue", "io.virtualan.cucumblan.core", "", path == null ? "conf/feature/" : path+"/feature/",
        };
    }

    /**
     * add conf to classpath
     *
     * @throws MalformedURLException
     */

    private static ExecutionClassloader addConfToClasspath(VirtualanClassLoader classLoader, String path) throws MalformedURLException {
        path = path == null ? "conf" : path;
        ExecutionClassloader cl = new ExecutionClassloader(new URL[] {new File(path).toURI().toURL()}, classLoader);
        Thread.currentThread().setContextClassLoader(cl);
        return cl;
    }

    /**
     * Read cucumblan properties.
     *
     * @param classLoader the class loader
     * @return the properties
     */
    public static Properties readCucumblan(ClassLoader classLoader) {
        Properties propertiesForInstance = new Properties();

        try {
            InputStream stream = classLoader.getResourceAsStream("cucumblan.properties");
            if (stream != null) {
                propertiesForInstance.load(stream);
            } else {
                LOGGER.warning("unable to load cucumblan.properties");
            }
        } catch (Exception var3) {
            LOGGER.warning("cucumblan.properties not found");
        }

        return propertiesForInstance;
    }

    /**
     * Generate the feature file for the provided collection
     *
     * @throws IOException
     */

    private static void generateFeatureFile(ExecutionClassloader classloader, ApiExecutorParam apiExecutorParam) throws IOException, UnableToProcessException {
        String path = apiExecutorParam.getOutputDir();
        if(path == null || !new File(path).exists()) {
            if (!new File("conf").exists()) {
                new File("conf").mkdir();
            }
            path = "conf";
        }
        if (!new File(path+"/feature").exists()) {
            new File(path+"/feature").mkdir();
        }
        Properties properties = readCucumblan(classloader);
        List<List<Item>> items = FeatureFileGenerator.generateFeatureFile(properties, apiExecutorParam);

        String featureTitle = properties.getProperty("virtualan.data.heading");

        for(int i=0; i< items.size(); i++){
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile("virtualan-contract.mustache");
            FileOutputStream outputStream = new FileOutputStream(path+"/feature/virtualan-contract."+i+".feature");
            Writer writer = new OutputStreamWriter(outputStream);
            mustache.execute(writer, new FeatureFileMapping(getTitle(featureTitle, i, feature), items.get(i))).flush();
            writer.close();
        }
    }

    private static String getTitle(String arrayTitle, int index , String defaultString){
        try{
            return arrayTitle.split(";")[index];
        }catch (Exception e){
            String featureTitle = ApplicationConfiguration.getProperty("virtualan.data.load");
            return removeFileName(featureTitle.split(";")[index]);
        }
    }

    private static String removeFileName(String featureTitle) {
        return featureTitle != null? featureTitle.replace(".json", "") :  featureTitle;
    }
}
