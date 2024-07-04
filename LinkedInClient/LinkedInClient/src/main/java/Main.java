import Controller.RetrofitBuilder;
import Controller.SignUpController;
import Model.*;
import Model.Requests.*;
import Model.Response.WatchConnectionListResponse;
import Model.Response.WatchConnectionPendingLists;
import Model.Response.WatchPostSearchResults;
import Model.Response.WatchProfileSearchResults;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParseException {
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder();

//        System.out.println(retrofitBuilder.syncCallSayHello().toString());
//        System.out.println(retrofitBuilder.syncCallGetUser().toString());

//        Messages signUpMessages = null;
//        if((signUpMessages = SignUpController.validateUserData("mmd23@yahoo.com", "tEST@123", "tEST@123",
//                "Goostavo")) == Messages.SUCCESS) {
//            RegisterCredentials registerCredentials = new RegisterCredentials("mmd23@yahoo.com", "tEST@123", "tEST@123", "Goostavo");
//            System.out.println(retrofitBuilder.syncCallSignUp(registerCredentials).getMessage());
//        }
//        else {
//            System.out.println(signUpMessages.getMessage());
//        }
//
//        if((signUpMessages = SignUpController.validateUserData("whatthehell@yahoo.com", "teteEST@123",
//                "teteEST@123", "testProject")) == Messages.SUCCESS) {
//            RegisterCredentials registerCredentials = new RegisterCredentials("whatthehell@yahoo.com", "teteEST@123", "teteEST@123", "testProject");
//            System.out.println(retrofitBuilder.syncCallSignUp(registerCredentials).getMessage());
//        }
//        else {
//            System.out.println(signUpMessages.getMessage());
//        }


//        LoginCredentials loginCredentials = new LoginCredentials("Goostavo", "tEST@123");
//        Messages loginResp = retrofitBuilder.syncCallLogin(loginCredentials);
//        System.out.println(loginResp.getMessage());
//        System.out.println(Cookies.getSessionToken());

//
        LoginCredentials loginCredentials = new LoginCredentials("testProject", "teteEST@123");
        Messages loginResp = retrofitBuilder.syncCallLogin(loginCredentials);
        System.out.println(loginResp.getMessage());
        System.out.println(Cookies.getSessionToken());
//
//        ProfileExperience profileExperience;//###################################
//        List<ProfileJob> jobs = new ArrayList<>();
//        ProfileJob profileJob = new ProfileJob("Noob", JobStatus.FULL_TIME, "Behsa",
//                "Tehran", JobWorkplaceStatus.ON_SITE, true,
//                new SimpleDateFormat("yyyy-MM").parse("2023-11"),
//                new SimpleDateFormat("yyyy-MM").parse("2024-12"),
//                false, "Salooom", Arrays.asList(JobSkills.ARCHITECTURE, JobSkills.CAD, JobSkills.CHANGE_MANAGEMENT),
//                false, false);
//        jobs.add(profileJob);
//        ProfileJob profileJob2 = new ProfileJob("XXX Developer", JobStatus.INTERNSHIP, "MCI",
//                "Tehran", JobWorkplaceStatus.REMOTE, true,
//                new SimpleDateFormat("yyyy-MM").parse("2022-11"),
//                new SimpleDateFormat("yyyy-MM").parse("2023-11"),
//                false, "Salooom", Arrays.asList(JobSkills.CLOUD_COMPUTING, JobSkills.DEVOPS),
//                false, false);
//        jobs.add(profileJob2);
//        List<ProfileVoluntaryActivities> voluntaryActivities = new ArrayList<>();
//        ProfileVoluntaryActivities profileVoluntaryActivities = new ProfileVoluntaryActivities("charity",
//                new SimpleDateFormat("yyyy-MM").parse("2023-11"));
//        voluntaryActivities.add(profileVoluntaryActivities);
//        String militaryService = "General";
//        Date militaryServiceDate = new SimpleDateFormat("yyyy-MM").parse("2024-12");
//        String ceoExperience = "Yah";
//        Date ceoExperienceDate = new SimpleDateFormat("yyyy-MM").parse("2024-12");
//        List<ProfileSports> profileSports = new ArrayList<>();
//        ProfileSports profileSport = new ProfileSports("wwe", new SimpleDateFormat("yyyy-MM").parse("2024-12"));
//        profileSports.add(profileSport);
//        profileExperience = new ProfileExperience(jobs, voluntaryActivities, militaryService, militaryServiceDate, ceoExperience,
//                ceoExperienceDate, profileSports);
//
//        List<ProfileEducation> profileEducations = new ArrayList<>();//############################################
//        ProfileEducation profileEducation = new ProfileEducation("Mehr Akhar",
//                new SimpleDateFormat("yyyy-MM").parse("2024-12")
//                ,new SimpleDateFormat("yyyy-MM").parse("2025-01"), false,
//                "15.3", "modir majale", "yo", Arrays.asList(EducationalSkills.EDUCATION),
//                false, false);
//        ProfileEducation profileEducation2 = new ProfileEducation("Mehr Akhar",
//                new SimpleDateFormat("yyyy-MM").parse("2024-12")
//                ,new SimpleDateFormat("yyyy-MM").parse("2025-01"),
//                false, "15.3", "modir majale",
//                "yo", Arrays.asList(EducationalSkills.BIOLOGY, EducationalSkills.ACCOUNTING),
//                false, true);
//        profileEducations.add(profileEducation);
//
//        List<Certificate> certificates = new ArrayList<>();//#####################################
//        Certificate certificate = new Certificate("Pro Player G+", "ESL",
//                new SimpleDateFormat("yyyy-MM").parse("2023-01"),
//                new SimpleDateFormat("yyyy-MM").parse("2023-04"), "UCL 22-12",
//                "www.seyyx.com", Arrays.asList("Sex","Aim"));
//        certificates.add(certificate);
//
//        ProfileJob currentJob = new ProfileJob("Sex Developer", JobStatus.FULL_TIME,
//                "Behsa",
//                "Tehran", JobWorkplaceStatus.ON_SITE,
//                true,
//                new SimpleDateFormat("yyyy-MM").parse("2024-11"),
//                null,
//                true, "Salooom", Arrays.asList(JobSkills.ARCHITECTURE, JobSkills.CAD, JobSkills.CHANGE_MANAGEMENT),
//                false, true);
//        ProfileContactInfo profileContactInfo = new ProfileContactInfo("aaa.aaa.com", "a@A.com", "0912", PhoneType.MOBILE_PHONE,
//                "saghez", new SimpleDateFormat("yyyy-MM").parse("2004-11"), ShowBirthDateTo.ALL, "tel.me/lesbian");
//        ProfileHeader profileHeader = new ProfileHeader("Moein", "Enayati", null, "aaa.aaa.com", "aaa.vvv.com", "man gay hastam", currentJob, profileEducation2,
//                "Iran", "Tehran", "Engineer", profileContactInfo, "Sexy"); //#################
//
//
//        ProfileSkills profileSkills = new ProfileSkills(Arrays.asList(JobSkills.DEVOPS, JobSkills.CLOUD_COMPUTING), Arrays.asList(EducationalSkills.DESIGN, EducationalSkills.BIOLOGY)); //######
//        ProfileOrganizations profileOrganizations = new ProfileOrganizations("OnlyFans", "actor", new SimpleDateFormat("yyyy-MM").parse("2024-11"), null, true);
//
//
//
//        CreateProfileRequest profile = new CreateProfileRequest(profileExperience, profileEducations, certificates, profileHeader, profileSkills, profileOrganizations);
//        Messages makeProfile = retrofitBuilder.syncCallProfile(profile);
//        System.out.println(makeProfile.getMessage());

//        retrofitBuilder.asyncCallUpload("src/main/resources/test2.mp4");
//        retrofitBuilder.asyncCallDownload("test2.mp4");
//
//        SearchProfileRequest searchProfileRequest = new SearchProfileRequest("Moein",null,"Tehran",null,null,null,null);
//        WatchProfileSearchResults watchProfileSearchResults = retrofitBuilder.watchProfileSearchResults(searchProfileRequest);
//        System.out.println(watchProfileSearchResults);
//        ConnectRequest connectRequest = new ConnectRequest(2, "Be man accept Bedahid");
//        Messages messages = retrofitBuilder.syncCallConnect(connectRequest);
//        WatchConnectionPendingLists watchConnectionPendingLists = retrofitBuilder.watchConnectionPendingLists();
//        System.out.println(watchConnectionPendingLists.toString());
//        WatchConnectionListResponse watchConnectionListResponse = retrofitBuilder.watchConnectionListResponse();
//        System.out.println(watchConnectionListResponse.toString());

//        AcceptConnection acceptConnection = new AcceptConnection(true, 1);
//        Messages messages = retrofitBuilder.acceptConnection(acceptConnection);
//        System.out.println(messages.toString());
//        WatchConnectionListResponse watchConnectionListResponse = retrofitBuilder.watchConnectionListResponse();
//        System.out.println(watchConnectionListResponse.toString());
//        WatchConnectionPendingLists watchConnectionPendingLists = retrofitBuilder.watchConnectionPendingLists();
//        System.out.println(watchConnectionPendingLists.toString());

//        String mediaName = retrofitBuilder.asyncCallUpload("src/main/resources/test2.mp4");
//        Post post = new Post();
//        post.setText("Hi bitches");
//        List<String> hashtags = new ArrayList<>();
//        hashtags.add("kir");
//        hashtags.add("kos");
//        hashtags.add("koon");
//        post.setHashtags(hashtags);
//        post.setMediaName(mediaName);
//        retrofitBuilder.syncCallPost(post);

        WatchPostSearchResults watchPostSearchResults = retrofitBuilder.searchPostRequest(new SearchPostsRequest("Hi"));
        System.out.println(watchPostSearchResults.toString());
        retrofitBuilder.asyncCallDownload(watchPostSearchResults.getPosts().get(0).getMediaName());
    }
}
