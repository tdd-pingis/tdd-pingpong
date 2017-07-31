[33mcommit 4cfcf7a820a2775f046a2cf1ff7eb0311c5612ff[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Fri Jul 28 13:28:26 2017 +0300

    fix ImplementationType.java and type-variable in TaskImplementation

[33mcommit 2bd732072ad1a1685f05affd5a284b467d5630d6[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Fri Jul 28 13:14:19 2017 +0300

    database refactored, ChallengeImplementation added

[33mcommit e14d3ae8844154479ec477c84966072b1678e450[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Fri Jul 28 11:00:21 2017 +0300

    database refactored to support new db-schema

[33mcommit 02315d1982cf310be1f1fbc708e45ef459a9167a[m
Merge: d812466 6f9dd69
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Fri Jul 28 11:32:20 2017 +0300

    Merge pull request #46 from dwarfcrank/tmc-config
    
    Use application.properties for TMC sandbox URL configuration.

[33mcommit 6f9dd696614093b0d177db6fc8046ed2ab46ac99[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Fri Jul 28 11:25:28 2017 +0300

    Use application.properties for TMC sandbox URL configuration.

[33mcommit d8124668f59c01fce9d9d70da097889d9c986ca1[m
Merge: edbfb21 91f11b8
Author: laurpulk <lauri.pulkkinen@helsinki.fi>
Date:   Thu Jul 27 14:48:24 2017 +0300

    Merge pull request #45 from dwarfcrank/send-submission
    
    Send submission

[33mcommit 91f11b83df8a2969f18d4723c9072ce143dca147[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Thu Jul 27 14:21:27 2017 +0300

    Basic implementation of SubmissionSenderService.

[33mcommit f0565bf97db1940da6ba70e23b69d875bc2008f6[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Thu Jul 27 14:17:42 2017 +0300

    Add Jackson Databind as a dependency.

[33mcommit edbfb213b59ba3a80e2e48d76cd8b6e8acce269f[m
Merge: eef67bc 433b2a0
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Thu Jul 27 10:20:05 2017 +0300

    Merge pull request #44 from Heliozoa/oauth2-setup
    
    OAuth2 setup

[33mcommit 433b2a050566ac13ad3bdb4434d4d53ab368cbce[m
Merge: 9adf338 eef67bc
Author: laurpulk <lauri.pulkkinen@helsinki.fi>
Date:   Thu Jul 27 10:14:35 2017 +0300

    Merge branch 'master' into oauth2-setup

[33mcommit eef67bc832e1af4158769c74d04f7a2b65a1451b[m
Merge: df6d69e 52e9239
Author: laurpulk <lauri.pulkkinen@helsinki.fi>
Date:   Thu Jul 27 10:08:56 2017 +0300

    Merge pull request #38 from DeepIntuition/dataloader-refactor
    
    Dataloader refactoring, new javaparser class and navbar

[33mcommit 9adf338dbf5faec7a4216ebb800e19dd7f8927ff[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Wed Jul 26 23:12:13 2017 +0300

    Updated dependencies for Spring 5.0's OAuth2 support

[33mcommit 75479b93e7ff8b1740fdde815a40b4398b037412[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Wed Jul 26 23:11:30 2017 +0300

    Updated Gradle to 4.0.2

[33mcommit 52e92392b86f5b7db835e6140037bd6e1d840c20[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Wed Jul 26 12:02:56 2017 +0300

    JavaClassParser name changed to JavaClassGenerator

[33mcommit df6d69ec2b92c17984ffe2ed6a712582e82aaacc[m
Merge: 56f8684 f0b2b35
Author: DeepIntuition <ville.saarinen@helsinki.fi>
Date:   Wed Jul 26 11:58:55 2017 +0300

    Merge pull request #39 from dwarfcrank/submission-packaging
    
    Implement submission packaging.

[33mcommit 1bd37f0c8c556904474174c993c1a44c93fc571e[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Wed Jul 26 11:24:24 2017 +0300

    tests for JavaClassParser.java

[33mcommit f0b2b35ad69d3a6e470da1a215453132deb887ce[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Wed Jul 26 10:55:40 2017 +0300

    Add Javadocs for packageSubmission().

[33mcommit 8f439f72ebba4142ef47e7570eb08a0b6b9f4147[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Wed Jul 26 10:51:22 2017 +0300

    Refactor addTemplateFiles and addAdditionalFiles slightly.

[33mcommit 517ba9245564c854d4115098e581f77ba74b4d11[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Wed Jul 26 10:45:17 2017 +0300

    Improve packaging tests.

[33mcommit 8bb5c60924c8229d9a4d300516cae1c5b52e989c[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Tue Jul 25 14:44:44 2017 +0300

    Fix checkstyle.

[33mcommit a6cabaa2867d4295a3367cce98ce0a00af993bfa[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Tue Jul 25 14:33:20 2017 +0300

    Implement SubmissionPackagingService.

[33mcommit 77bfaa4694974b93126ea8e4c9e3ef4165b37336[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Tue Jul 25 11:17:23 2017 +0300

    Add an example exercise and its dependencies as the base for a project template.

[33mcommit 9aec44418cb5d00486f11bdb7a7817c4b6764ef1[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Wed Jul 26 11:15:29 2017 +0300

    Add Apache Commons Compress as a dependency.

[33mcommit 56f8684b197528f5f50db5a67d2fd95880dd5edc[m
Merge: a7f2d83 074aab2
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Wed Jul 26 10:49:23 2017 +0300

    Merge pull request #41 from Heliozoa/selenium-setup
    
    Simple login functionality for two sample users

[33mcommit a7f2d83380b54734ebe22b1df4dfeec23c54782a[m
Merge: 75028aa a199df8
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Wed Jul 26 10:43:06 2017 +0300

    Merge pull request #42 from Heliozoa/database-fix
    
    Changed the H2 database to be in-memory.

[33mcommit a199df845d36e0df9b5eac5dae5c2edecef9ab85[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Wed Jul 26 10:40:44 2017 +0300

    Changed the H2 database to be in-memory.

[33mcommit 75028aae9ed941448425ea5bbce7947786011c78[m
Merge: 26bc61f 605edf0
Author: laurpulk <lauri.pulkkinen@helsinki.fi>
Date:   Wed Jul 26 10:14:16 2017 +0300

    Merge pull request #40 from DeepIntuition/master
    
    restructure and update foreign keys and primary keys into db schema

[33mcommit 074aab2002ea809062024e093f42cd803cc8d7d9[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Wed Jul 26 03:26:23 2017 +0300

    Fixed an error in a cucumber feature's name

[33mcommit c2a57ca4d7f3735aa745886ca47295f2fba55855[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Wed Jul 26 03:02:02 2017 +0300

    Set up Selenium

[33mcommit 605edf066f4bd474fde1a8e61a663eb7eef67382[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Tue Jul 25 23:12:18 2017 +0300

    restructure and update foreign keys and primary keys into db schema

[33mcommit 04eda8ccff05326d1d77e0f5995247c7e3bcb243[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Tue Jul 25 21:58:41 2017 +0300

    Simple login functionality with Spring Security

[33mcommit 70c8353aa25c4eb8a7111d4c003b405e4be95c06[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Tue Jul 25 14:07:42 2017 +0300

    added jUnit tests for JavaClassParser.java

[33mcommit 7f5d96586cfff14abd9c9d4fcfa6e007d5b13ffb[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Tue Jul 25 12:09:17 2017 +0300

    Proper class parsing added

[33mcommit 26bc61f0f14d0e747fb4605e511eb526f268273a[m
Merge: 3fec20c 2fef1a0
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Tue Jul 25 11:56:06 2017 +0300

    Merge pull request #37 from Heliozoa/spring-security
    
    Spring security dependencies added

[33mcommit 2fef1a0d21185076e29ac6ccfa23f78c83a29422[m
Merge: 76ecb67 3fec20c
Author: Heliozoa <dm89132@gmail.com>
Date:   Tue Jul 25 11:54:11 2017 +0300

    Merged build.gradle changes

[33mcommit 76ecb67aa75ba6c70aca79bd98c2bf448fc4ff49[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Tue Jul 25 11:50:42 2017 +0300

    Unified build.gradle syntax

[33mcommit 0cb705f095dcbd4b119378582e4f22eb23fd8d6d[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Tue Jul 25 11:48:57 2017 +0300

    Spring Security dependencies added

[33mcommit 3fec20c4f763ebde4e9db0e026f8fe1174346db5[m
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Tue Jul 25 10:45:31 2017 +0300

    Fix travis docker integration

[33mcommit 259b2b33250ab0741b627c40a8cd355584ed2085[m
Merge: 09d7ece 83045c3
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Tue Jul 25 10:43:25 2017 +0300

    Merge pull request #35 from tdd-pingis/dev
    
    Sprint 1 milestone

[33mcommit 1682cdef470b4283b335b703d7fd1889eddf06a9[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Tue Jul 25 10:13:24 2017 +0300

    Dataloader and task.java refactoring

[33mcommit 83045c3f6dcf462301b33dfd251efdc38ce2f2dd[m
Author: Lauri Pulkkinen <lauri.pulkkinen@helsinki.fi>
Date:   Mon Jul 24 14:23:07 2017 +0300

    asiakaspalaveri 24.7.

[33mcommit 6e48cf2a85f229dbee4200307952ca1fd060c699[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Mon Jul 24 10:14:19 2017 +0300

    Formatted the example data to properly function with the java parser

[33mcommit 1d7d8f73e1d520f8fd9f57e1f2f201da7c8b4aaf[m
Merge: 76055f5 692a5a0
Author: DeepIntuition <ville.saarinen@helsinki.fi>
Date:   Mon Jul 24 09:25:19 2017 +0300

    Merge pull request #29 from dwarfcrank/gitignore
    
    Update gitignore with IntelliJ workaround.

[33mcommit 76055f5af4bea68cb125c7829cf75839ca8a60e1[m
Merge: d6c0f2c c27ffa0
Author: DeepIntuition <ville.saarinen@helsinki.fi>
Date:   Mon Jul 24 09:24:50 2017 +0300

    Merge pull request #28 from dwarfcrank/syntax-check
    
    Implement support for checking submission syntax.

[33mcommit 692a5a08c3cf9907c1150b11a25657475bbf4882[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Fri Jul 21 19:28:27 2017 +0300

    Update gitignore with IntelliJ workaround.

[33mcommit c27ffa08fa1b96d1c818e41ddbb9a30284d60b3e[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Fri Jul 21 16:18:15 2017 +0300

    Fix checkstyle complaints.

[33mcommit c260e96b196134f14ab844f0347c1a80a1c3e91c[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Fri Jul 21 16:10:51 2017 +0300

    Add JavaDoc for JavaSyntaxChecker.

[33mcommit 2944913be32b82a0bbaf9b2178569e00c6d2ed63[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Fri Jul 21 16:02:45 2017 +0300

    Implement syntax checking in ChallengeController.

[33mcommit 8a22581a68f0026d1b8116995a587b17164502a3[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Fri Jul 21 14:21:15 2017 +0300

    Implement a class for Java syntax checking.

[33mcommit 09d7eceeefefcc80ea8c37dd8141be3fcc265513[m
Merge: c57e7b5 d6c0f2c
Author: Martinez <Heliozoa@users.noreply.github.com>
Date:   Fri Jul 21 14:52:54 2017 +0300

    Merge pull request #27 from tdd-pingis/dev
    
    Daily dev to master merge

[33mcommit d6c0f2cfdf9f73dc941bc59ed3a4b28ab98348a6[m
Merge: 12f164d b24eef2
Author: laurpulk <lauri.pulkkinen@helsinki.fi>
Date:   Fri Jul 21 14:48:55 2017 +0300

    Merge pull request #26 from DeepIntuition/dev
    
    added more dummy-data to Challenge 1 (Calculator)

[33mcommit b24eef2cd067161558c928e09f88a4fd69aba1a8[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Fri Jul 21 14:45:54 2017 +0300

    added more dummy-data to Challenge 1 (Calculator)

[33mcommit 12f164d1a9c98afb968963f05b3a24aae50ca450[m
Merge: 5758b9b 427d5cf
Author: Martinez <Heliozoa@users.noreply.github.com>
Date:   Fri Jul 21 14:43:52 2017 +0300

    Merge pull request #25 from laurpulk/dev
    
    Cucumber+spring fix

[33mcommit 427d5cf8a1c7836f73f0bddee33e0fbf39d56e84[m
Author: Lauri Pulkkinen <lauri.pulkkinen@helsinki.fi>
Date:   Fri Jul 21 14:31:06 2017 +0300

    Fix cucumber+spring

[33mcommit 5758b9b15ab896f2f2ca0f1ee28cc46f48b055b5[m
Merge: 2edad85 9eeaeb2
Author: laurpulk <lauri.pulkkinen@helsinki.fi>
Date:   Fri Jul 21 14:27:15 2017 +0300

    Merge pull request #24 from tdd-pingis/revert-23-dev
    
    Revert "Added dummy-data for Challenge 1 (Calculator)"

[33mcommit 9eeaeb2bba3006fbf96d2e5c4d69c64d13afa719[m
Author: Lauri Ramirez <lramirez@users.noreply.github.com>
Date:   Fri Jul 21 14:19:43 2017 +0300

    Revert "Added dummy-data for Challenge 1 (Calculator)"

[33mcommit 2edad853d3826364154883baf029fe59dff3235d[m
Merge: 625619d a90e8fe
Author: Lauri Ramirez <lramirez@users.noreply.github.com>
Date:   Fri Jul 21 14:18:56 2017 +0300

    Merge pull request #23 from DeepIntuition/dev
    
    Added dummy-data for Challenge 1 (Calculator)

[33mcommit a90e8fe4b21021d9c269086c4fa19ce00523b11a[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Fri Jul 21 14:02:42 2017 +0300

    added 5 more tasks to dummy-db

[33mcommit bb800d50d8f74377257142cb95e11a06ebf5227f[m
Merge: 8621f46 625619d
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Fri Jul 21 13:58:09 2017 +0300

    Merge branch 'dev' of https://github.com/tdd-pingis/tdd-pingpong into new-master

[33mcommit 625619d198f0fbcd9a471b19a8f831118005d9bf[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Fri Jul 21 13:15:27 2017 +0300

    checkstyle fixed again

[33mcommit d0344653c5e64fd1c4ee9b2a844e64585fcead7e[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Fri Jul 21 13:07:55 2017 +0300

    swp-file removed, checkstyle fixed

[33mcommit 980e5e3d0dd8cb363131c9953d808994f824f522[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Fri Jul 21 12:58:06 2017 +0300

    added more dummy-data and arguments for task controller method, new feedback-page

[33mcommit 8621f4673cf87796fba1c547562c1ab8a3942f7b[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Fri Jul 21 13:15:27 2017 +0300

    checkstyle fixed again

[33mcommit c2170a18449c5df377ab4665de13a59a63eb5704[m
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Fri Jul 21 13:08:29 2017 +0300

    Fix finally

[33mcommit c753ea24200b8e915ace4d4f1e3dd7bcfeffcdb7[m
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Thu Jul 20 18:03:08 2017 +0300

    Setup travis to run docker script on server after success

[33mcommit 1df519174d53029396ac85bfb49a2f469ca61d32[m
Merge: 5e01767 9bc4b24
Author: Martinez <Heliozoa@users.noreply.github.com>
Date:   Fri Jul 21 13:08:38 2017 +0300

    Merge pull request #22 from dwarfcrank/gitignore
    
    Update .gitignore.

[33mcommit 46bcb5ce15f23049e616b8afaf90039c27e5b27f[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Fri Jul 21 13:07:55 2017 +0300

    swp-file removed, checkstyle fixed

[33mcommit 9bc4b2415e50f393e79a6baad4700cb42b538d08[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Fri Jul 21 13:06:53 2017 +0300

    Update .gitignore.

[33mcommit ee6fdcffc86f60440f14c6d8dbb5616eb50e5e99[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Fri Jul 21 12:58:06 2017 +0300

    added more dummy-data and arguments for task controller method, new feedback-page

[33mcommit 5e01767e36cb534385ebcf3657adad6764c1eb79[m
Merge: 756d6eb 1504ac5
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Fri Jul 21 12:36:17 2017 +0300

    Merge pull request #19 from Heliozoa/cucumber-setup
    
    Cucumber setup and example feature & stepdefs

[33mcommit 1504ac5c4695373c338d5fe8cc182c79367bdba7[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Fri Jul 21 12:30:07 2017 +0300

    Cucumber setup and example feature & stepdefs

[33mcommit 756d6ebc390f34056a6fca97bd8d811bc5d3a5ea[m
Merge: 8974258 29736a9
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Fri Jul 21 10:59:02 2017 +0300

    Merge pull request #16 from dwarfcrank/travis-slack
    
    Add Travis->Slack integration.

[33mcommit 29736a997c957776ba1d9c3ed11fa90f50970ea7[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Fri Jul 21 10:52:19 2017 +0300

    Add Travis->Slack integration.

[33mcommit c57e7b59fc2b985fc71a45ef58477043b1ec0e19[m
Merge: dce8ff8 8974258
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Thu Jul 20 15:21:29 2017 +0300

    Merge pull request #15 from tdd-pingis/dev
    
    Merge dev to master.

[33mcommit 8974258d2561ca4832d7e527d5cab42a024c07c0[m
Merge: e4ff877 d8ef9a8
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Thu Jul 20 14:46:02 2017 +0300

    Merge pull request #14 from lramirez/master
    
    example data, view updated, entities updated

[33mcommit d8ef9a8d331e9c2fc847144d74179095b7c41fe6[m
Merge: ac12bc1 e4ff877
Author: lauri <lauri334@hotmail.com>
Date:   Thu Jul 20 14:42:13 2017 +0300

    testdata

[33mcommit ac12bc14797accca7b6576cf9a393453ac6f70b3[m
Author: lauri <lauri334@hotmail.com>
Date:   Thu Jul 20 14:33:23 2017 +0300

    test data inserted into database

[33mcommit e4ff877ae66d4d9e25c06931b6c9e718c5672ea3[m
Merge: c5b10b6 81b8c54
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Thu Jul 20 14:15:44 2017 +0300

    Merge branch 'dev' of https://github.com/tdd-pingis/tdd-pingpong into dev

[33mcommit c5b10b6033e548a73086ee769683cfa7b4a58473[m
Merge: 1d03e1d 0032230
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Thu Jul 20 13:45:45 2017 +0300

    Merge branch 'lramirez-master' into dev

[33mcommit 0032230e73d9beb15c763f12bd3d3b15f172611c[m
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Thu Jul 20 13:17:45 2017 +0300

    Run deploy.sh only on master

[33mcommit 0bbeeccb104e1c1b8471ac68d4d6c9ab4b727f37[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Thu Jul 20 11:38:26 2017 +0300

    Update travis/coveralls links in README.

[33mcommit a0ec4234b6e0a217421def3c27184b57a3fb0528[m
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Thu Jul 20 11:30:34 2017 +0300

    Configure travis ssh to server

[33mcommit 81b8c54499c653e95b937a6ee286d753dc1035e4[m
Author: Lauri Pulkkinen <lauri.pulkkinen@helsinki.fi>
Date:   Thu Jul 20 13:36:21 2017 +0300

    Renamed packages in entities

[33mcommit 53b12a3981564e4dd78cf86209836f74a9b802ab[m
Author: Lauri Pulkkinen <lauri.pulkkinen@helsinki.fi>
Date:   Thu Jul 20 13:14:02 2017 +0300

    Test skeletons

[33mcommit 00387a6d2b08e1f3b520ca20d55a8fb27f910262[m
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Thu Jul 20 13:28:06 2017 +0300

    Fix conditional deploy

[33mcommit 1d03e1d36605a578358152f93cff1d6fe8021cae[m
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Thu Jul 20 13:17:45 2017 +0300

    Run deploy.sh only on master

[33mcommit 9eae7d4434aadd937e10ea65e91f759d5f2a84ea[m
Author: lauri <lauri334@hotmail.com>
Date:   Thu Jul 20 13:08:15 2017 +0300

    database works, changed directory structure

[33mcommit beaa922ed9270e8b2ed359751e9b68bcb4384f9b[m
Author: lauri <lauri334@hotmail.com>
Date:   Thu Jul 20 13:07:54 2017 +0300

    database works, changed directory structure

[33mcommit 54bfb40c034fb5e1250734443f1b4b67831f1ea9[m
Merge: d54b798 5d0bae0
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Thu Jul 20 11:42:07 2017 +0300

    Merge pull request #12 from dwarfcrank/update-readme
    
    Update travis/coveralls links in README.

[33mcommit 5d0bae0c352da8efb7f729be6112e00e601996f5[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Thu Jul 20 11:38:26 2017 +0300

    Update travis/coveralls links in README.

[33mcommit 0ffbae9f8dc98f792f1cbd57c05e39f3256b2b7b[m
Merge: 56b93b5 b088c72
Author: lauri <lauri334@hotmail.com>
Date:   Thu Jul 20 11:36:56 2017 +0300

    Merge branch 'dev' of https://github.com/tdd-pingis/tdd-pingpong

[33mcommit d54b7981d8ab126c1a69971a27e595f7a3326902[m
Merge: d097868 b088c72
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Thu Jul 20 11:35:04 2017 +0300

    Merge branch 'dev' of https://github.com/tdd-pingis/tdd-pingpong into dev

[33mcommit d097868b4a2afe34d27ce6e271f23e1d480d45a2[m
Merge: 3754a03 e1acd20
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Thu Jul 20 11:32:55 2017 +0300

    Merge branch 'dev' of github.com:impliedfeline/tdd-pingpong into dev

[33mcommit 3754a0332846a545e0e90377682745cbb6f48ebf[m
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Thu Jul 20 11:30:34 2017 +0300

    Configure travis ssh to server

[33mcommit b088c725b4ca397a6dba1e594faa6f7ddf3fb5aa[m
Merge: f6f6aaa b05f2de
Author: DeepIntuition <ville.saarinen@helsinki.fi>
Date:   Thu Jul 20 11:30:28 2017 +0300

    Merge pull request #11 from dwarfcrank/example-cleanup
    
    Remove Spring Boot example code.

[33mcommit b05f2de02222f0a70e0ed5bd443ae51f0a772e43[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Thu Jul 20 11:18:45 2017 +0300

    Remove Spring Boot example code.

[33mcommit f6f6aaa62c54d8e18de65570fb9ce964837c5114[m
Merge: e1acd20 ed38a96
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Thu Jul 20 11:16:20 2017 +0300

    Merge pull request #10 from DeepIntuition/dev
    
    Removing unnecessary create tables sentences

[33mcommit ed38a965eff3dd66612b40ac249c4b26bf6453ce[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Thu Jul 20 10:55:26 2017 +0300

    removed unnecessary sql-folder

[33mcommit 56b93b5d7bf23cd2615ba0948c3cf5c75fb462c5[m
Merge: dce8ff8 e1acd20
Author: lauri <lauri334@hotmail.com>
Date:   Thu Jul 20 10:54:00 2017 +0300

    Merge branch 'dev' of https://github.com/tdd-pingis/tdd-pingpong

[33mcommit e07fc9fbbccb365d595d6f9bcf18bcee3c8b6cf3[m
Merge: 112e302 e1acd20
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Thu Jul 20 10:53:38 2017 +0300

    Merge branch 'dev' of https://github.com/tdd-pingis/tdd-pingpong

[33mcommit e1acd20cdcb6815f89ddab21df9cac4eb4bb9ff0[m
Merge: e54473a bcbe4a3
Author: lauri <lauri334@hotmail.com>
Date:   Thu Jul 20 10:26:14 2017 +0300

    Merge branch 'dev' of https://github.com/laurpulk/tdd-pingpong into dev

[33mcommit e54473ac7e3a0200b096b7e3c2882d5027bfeff4[m
Author: lauri <lauri334@hotmail.com>
Date:   Thu Jul 20 10:26:00 2017 +0300

    tehtavasivun sisalto dynaaminen

[33mcommit dce8ff84adfa70ad8c73a6c3bac04e6f8b3a8289[m
Merge: 112e302 bcbe4a3
Author: DeepIntuition <ville.saarinen@helsinki.fi>
Date:   Thu Jul 20 10:22:45 2017 +0300

    Merge pull request #9 from laurpulk/dev
    
    Dev and master merged in order to ease the forking and avoid merge-issues

[33mcommit bcbe4a3da38d29ac6832828d827c81a6a2da892a[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Thu Jul 20 10:06:18 2017 +0300

    Checkstyle indentation error fixed

[33mcommit e1d43652e46231ee068469d3ab5297ebd75384a8[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Thu Jul 20 10:06:03 2017 +0300

    Additional netbeans project files added to gitignore

[33mcommit c6c529053095e0d505045f3cc4681d995ed1ba1a[m
Merge: ad8e379 bea08da
Author: Heliozoa <dm89132@gmail.com>
Date:   Thu Jul 20 10:00:10 2017 +0300

    Merge branch 'dev' of github.com:laurpulk/tdd-pingpong into dev

[33mcommit bea08da14c01579b287af8cad826cd650c1f265d[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Thu Jul 20 00:50:42 2017 +0300

    create_tables.sql MariaDB syntax fix

[33mcommit f7263b2d9d3b542ff1861bba0b6c88bcf05e7988[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Thu Jul 20 00:42:24 2017 +0300

    added create_tables.sql for database initialization

[33mcommit ad8e37981f52505f6070baae515b12beb5114500[m
Merge: 3bac5e5 a23d960
Author: Heliozoa <dm89132@gmail.com>
Date:   Wed Jul 19 14:54:46 2017 +0300

    Merge branch 'dev' of github.com:laurpulk/tdd-pingpong into dev

[33mcommit 3bac5e549dd415fc56dde4c2fcafb2d89abc8990[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Wed Jul 19 14:54:34 2017 +0300

    Entities whitespace changed to be consistent

[33mcommit a23d9600acdc571daed25c5b2cced373f291257c[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Wed Jul 19 14:50:51 2017 +0300

    checkstyle added

[33mcommit 126cdaab7987183be7a1f8195c71c0143b912065[m
Author: lauri <lauri334@hotmail.com>
Date:   Wed Jul 19 14:43:21 2017 +0300

    checkstyle

[33mcommit a3a0a0dd2a2baf26c7acff461919cbed077aa179[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Wed Jul 19 14:40:19 2017 +0300

    Entities package fixed to match dir structure

[33mcommit c6885ec2e7951bd2a91212d64613ce61fbb445f3[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Wed Jul 19 14:36:55 2017 +0300

    Entities import fixes

[33mcommit 346175180d52a09984889686702ac6e9d27f9a0c[m
Author: Heliozoa <dm89132@gmail.com>
Date:   Wed Jul 19 14:29:13 2017 +0300

    Java objects for database entities

[33mcommit 2d30ed5df5289f41304280b5a9cf2c45a0dfb10b[m
Author: Lauri Pulkkinen <lauri.pulkkinen@helsinki.fi>
Date:   Wed Jul 19 14:24:28 2017 +0300

    Added submit button for Ace-editor

[33mcommit 112e302c6d844e0bba1af0e9f8eacb56e6480073[m
Author: DeepIntuition <ville.saarinen@helsinki.fi>
Date:   Wed Jul 19 14:24:17 2017 +0300

    database_schema_v0.1b.png

[33mcommit b9b78aab85e8221d9ea83fbf591372c6d888f564[m
Author: DeepIntuition <ville.saarinen@helsinki.fi>
Date:   Wed Jul 19 14:22:36 2017 +0300

    database schema updated

[33mcommit bd8faf8dd7b2fbe2a5347ae48355dec5edf443c5[m
Author: DeepIntuition <ville.saarinen@helsinki.fi>
Date:   Wed Jul 19 13:27:42 2017 +0300

    adding database schema

[33mcommit ef6bf04ec2ed39ced282dc74befc822972fcb2f5[m
Author: Lauri Pulkkinen <lauri.pulkkinen@helsinki.fi>
Date:   Wed Jul 19 10:59:13 2017 +0300

    Embed Ace-Editor

[33mcommit 65ddfb4c8245e109a7c0269ec552e9a7236d7ecb[m
Author: lauri <lauri334@hotmail.com>
Date:   Wed Jul 19 11:18:06 2017 +0300

    assignment-page

[33mcommit 712823d5f92a402deb26125360e09947c157f1f4[m
Merge: e3ca789 8a48b1a
Author: lauri <lauri334@hotmail.com>
Date:   Wed Jul 19 10:57:37 2017 +0300

    Merge branch 'dev' of https://github.com/laurpulk/tdd-pingpong into dev

[33mcommit e3ca789836a560ff6145acd7b94e4c378167ca1f[m
Author: lauri <lauri334@hotmail.com>
Date:   Wed Jul 19 10:57:10 2017 +0300

    navbar added

[33mcommit 8a48b1a398b12fa59d71440d03945afe86c26a6e[m
Author: Lauri Pulkkinen <lauri.pulkkinen@helsinki.fi>
Date:   Wed Jul 19 10:08:24 2017 +0300

    Added url for backlog

[33mcommit 4a02fb7fce64703b889493fb4dbbe5ee93a3ce78[m
Merge: f313ac6 ac56723
Author: lauri <lauri334@hotmail.com>
Date:   Tue Jul 18 14:43:12 2017 +0300

    Merge branch 'dev' of https://github.com/laurpulk/tdd-pingpong into dev

[33mcommit f313ac6759b13c3e690af460a5372010cd3ffd07[m
Author: lauri <lauri334@hotmail.com>
Date:   Tue Jul 18 14:42:51 2017 +0300

    säätöä

[33mcommit ac56723a29c01cedf41ac2f0e091f9385edd8652[m
Author: laurpulk <lauri.pulkkinen@helsinki.fi>
Date:   Tue Jul 18 14:42:23 2017 +0300

    Added coverage badge

[33mcommit d549076b92bc6fd0e3c51c3f713de58adfc0f4b1[m
Author: laurpulk <lauri.pulkkinen@helsinki.fi>
Date:   Tue Jul 18 14:25:11 2017 +0300

    added jacocoTestReport

[33mcommit d7123fb1a292b1bd0461aa5b8fc18111dd2bf8f3[m
Merge: fa1785c a85c7d1
Author: DeepIntuition <ville.saarinen@helsinki.fi>
Date:   Tue Jul 18 14:20:00 2017 +0300

    Merge pull request #8 from laurpulk/dev
    
    Dev

[33mcommit a85c7d1363fe4de2bd3b49bc81fe9bd3a4522392[m
Merge: 406e667 fa1785c
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Tue Jul 18 14:10:34 2017 +0300

    Merge branch 'dev' of github.com:laurpulk/tdd-pingpong into dev

[33mcommit 406e6677271eb786bc7bf49658996327e45af781[m
Author: Ville Saarinen <ville.saarinen@helsinki.fi>
Date:   Tue Jul 18 14:08:07 2017 +0300

    coveralls config

[33mcommit fa1785ced6f0d1ff655d7b19c163c142981db897[m
Author: DeepIntuition <ville.saarinen@helsinki.fi>
Date:   Tue Jul 18 14:03:23 2017 +0300

    Update .travis.yml

[33mcommit 7a2b3d7e12e06a7e87660dab20c95d718fa3196a[m
Author: DeepIntuition <ville.saarinen@helsinki.fi>
Date:   Tue Jul 18 14:03:13 2017 +0300

    Update .travis.yml

[33mcommit ae129a7f864e85b071c9e17bd072aa093e95b96f[m
Merge: 4149e89 3da6526
Author: lauri <lauri334@hotmail.com>
Date:   Tue Jul 18 11:51:46 2017 +0300

    templates

[33mcommit 4149e8989e39e2d98664a73f4e4ae9c54a37ba62[m
Author: lauri <lauri334@hotmail.com>
Date:   Tue Jul 18 11:48:56 2017 +0300

    added base template

[33mcommit 3da652645c286270b9387369c78df18d08fc8618[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Tue Jul 18 11:43:50 2017 +0300

    Update .gitignore for NetBeans generated files.

[33mcommit 285add3883064ac8f1589af1a1eb88073d25de35[m
Author: laurpulk <lauri.pulkkinen@helsinki.fi>
Date:   Tue Jul 18 10:45:36 2017 +0300

    jdk8

[33mcommit 01a56091a85b80dff8e1b7b1b96ba1d36dd1a636[m
Author: laurpulk <lauri.pulkkinen@helsinki.fi>
Date:   Tue Jul 18 10:40:54 2017 +0300

    travis.yml & jacoco

[33mcommit b66939956353cb2fbe5790d4f8104890dbc45891[m
Author: lauri <lauri334@hotmail.com>
Date:   Tue Jul 18 10:35:15 2017 +0300

    added bootstrap and stuff

[33mcommit 779859943ea7acf8a520c3ca6975a7676a47ad19[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Mon Jul 17 16:04:32 2017 +0300

    Add Spring Boot skeleton.

[33mcommit 54b2acdf806679b329ca7aaab8b224ed2c980017[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Mon Jul 17 16:04:01 2017 +0300

    Remove .gitignored files.

[33mcommit 4abbae51d156b9270fae7927cdf02b40a5eb65ab[m
Author: Matias Juntunen <matias.juntunen@gmail.com>
Date:   Mon Jul 17 12:43:29 2017 +0300

    Add .gitignore

[33mcommit 57b868961878568365825cbb2b18d264106c1f2d[m
Author: impliedfeline <kinnunen.jussi@hotmail.com>
Date:   Mon Jul 17 12:41:39 2017 +0300

    Dokumentoi asiakaspalaveri

[33mcommit 8b7a75ed40d95c5c67c8bebc57e330be5143e048[m
Author: lauri <lauri334@hotmail.com>
Date:   Mon Jul 17 12:30:03 2017 +0300

    projekti luotu

[33mcommit 41517fb1ed73e26d56b76d978b1a6eff0385c016[m
Author: laurpulk <lauri.pulkkinen@helsinki.fi>
Date:   Mon Jul 17 10:38:31 2017 +0300

    Initial commit
