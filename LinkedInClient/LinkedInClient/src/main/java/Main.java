import Controller.RetrofitBuilder;
import Model.*;
import Model.Requests.LoginCredentials;
import Model.Requests.CreateProfileRequest;

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
//
//        Messages signUpMessages = null;
//        if((signUpMessages = SignUpController.validateUserData("mmd23@yahoo.com", "tEST@123", "tEST@123", "Goostavo")) == Messages.SUCCESS) {
//            System.out.println(retrofitBuilder.syncCallSignUp(SignUpController.newUser("mmd23@yahoo.com", "tEST@123", "tEST@123", "Goostavo")).message);
//        }
//        else {
//            System.out.println(signUpMessages.message);
//        }
//
//        if((signUpMessages = SignUpController.validateUserData("whatthehell@yahoo.com", "teteEST@123", "teteEST@123", "testProject")) == Messages.SUCCESS) {
//            System.out.println(retrofitBuilder.syncCallSignUp(SignUpController.newUser("whatthehell@yahoo.com", "teteEST@123", "teteEST@123", "testProject")).message);
//        }
//        else {
//            System.out.println(signUpMessages.message);
//        }


        LoginCredentials loginCredentials = new LoginCredentials("Goostavo", "tEST@123");
        Messages loginResp = retrofitBuilder.syncCallLogin(loginCredentials);
        System.out.println(Cookies.getSessionToken());

//        ProfileExperience profileExperience;//###################################
//        List<ProfileJob> jobs = new ArrayList<>();
//        ProfileJob profileJob = new ProfileJob("Java Developer", JobStatus.FULL_TIME, "Behsa",
//                "Tehran", JobWorkplaceStatus.ON_SITE, true,
//                new SimpleDateFormat("yyyy-MM").parse("2023-11"),
//                new SimpleDateFormat("yyyy-MM").parse("2024-12"),
//                false, "Salooom", Arrays.asList(JobSkills.EMPTY),
//                false, false);
//        jobs.add(profileJob);
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
//        ProfileEducation profileEducation = new ProfileEducation("Mehr Avval", new SimpleDateFormat("yyyy-MM").parse("2024-12")
//                ,new SimpleDateFormat("yyyy-MM").parse("2025-01"), false, "15.3", "modir majale", "yo", Arrays.asList(EducationalSkills.EMPTY),
//                false, false);
//        ProfileEducation profileEducation2 = new ProfileEducation("Mehr Avval", new SimpleDateFormat("yyyy-MM").parse("2024-12")
//                ,new SimpleDateFormat("yyyy-MM").parse("2025-01"), false, "15.3", "modir majale", "yo", Arrays.asList(EducationalSkills.EMPTY),
//                false, true);
//        profileEducations.add(profileEducation);
//
//        List<Certificate> certificates = new ArrayList<>();//#####################################
//        Certificate certificate = new Certificate("Pro Player G+", "ESL", new SimpleDateFormat("yyyy-MM").parse("2023-01"),
//                new SimpleDateFormat("yyyy-MM").parse("2023-04"), "UCL 22-12", "www.sex.com", Arrays.asList("Sex","Aim"));
//        certificates.add(certificate);
//
//        ProfileJob currentJob = new ProfileJob("Java Developer", JobStatus.FULL_TIME, "Behsa",
//                "Tehran", JobWorkplaceStatus.ON_SITE, true,
//                new SimpleDateFormat("yyyy-MM").parse("2024-11"),
//                null,
//                true, "Salooom", Arrays.asList(JobSkills.EMPTY),
//                false, true);
//        ProfileContactInfo profileContactInfo = new ProfileContactInfo("aaa.aaa.com", "a@A.com", "0912", PhoneType.MOBILE_PHONE,
//                "saghez", new SimpleDateFormat("yyyy-MM").parse("2004-11"), ShowBirthDateTo.ALL, "tel.me/lesbian");
//        ProfileHeader profileHeader = new ProfileHeader("Parsa", "Enayati", null, "aaa.aaa.com", "aaa.vvv.com", "man gay hastam", currentJob, profileEducation2,
//                "Iran", "Karaj", "Engineer", profileContactInfo, "Sexy"); //#################
//
//
//        ProfileSkills profileSkills = new ProfileSkills(Arrays.asList(JobSkills.EMPTY), Arrays.asList(EducationalSkills.EMPTY)); //######
//        ProfileOrganizations profileOrganizations = new ProfileOrganizations("Brazzers", "actor", new SimpleDateFormat("yyyy-MM").parse("2024-11"), null, true);
//
//
//
//        CreateProfileRequest profile = new CreateProfileRequest(profileExperience, profileEducations, certificates, profileHeader, profileSkills, profileOrganizations);
//        Messages makeProfile = retrofitBuilder.syncCallProfile(profile);
//        System.out.println(makeProfile.getMessage());

//        retrofitBuilder.asyncCallUpload("src/main/resources/test2.mp4");
        retrofitBuilder.asyncCallDownload("test2.mp4");
    }
}
