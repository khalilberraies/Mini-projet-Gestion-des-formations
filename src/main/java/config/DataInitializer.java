package com.isi.gestion_formation.config;

import com.isi.gestion_formation.entity.*;
import com.isi.gestion_formation.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository           roleRepo;
    private final UtilisateurRepository    utilisateurRepo;
    private final DomaineRepository        domaineRepo;
    private final ProfilRepository         profilRepo;
    private final StructureRepository      structureRepo;
    private final EmployeurRepository      employeurRepo;
    private final FormateurRepository      formateurRepo;
    private final FormationRepository      formationRepo;
    private final ParticipantRepository    participantRepo;

    @Override
    public void run(String... args) {
        if (roleRepo.count() > 0) {
            System.out.println("✅ Base de données déjà initialisée — DataInitializer ignoré.");
            return;
        }

        System.out.println("🚀 Initialisation des données de démonstration...");

        // 1. RÔLES
        Role roleAdmin  = roleRepo.save(new Role(null, "ADMIN"));
        Role roleResp   = roleRepo.save(new Role(null, "RESPONSABLE"));
        Role roleUser   = roleRepo.save(new Role(null, "UTILISATEUR"));

        // 2. UTILISATEURS
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        utilisateurRepo.save(new Utilisateur(null, "admin",       encoder.encode("admin123"),   roleAdmin));
        utilisateurRepo.save(new Utilisateur(null, "responsable", encoder.encode("resp1234"),   roleResp));
        utilisateurRepo.save(new Utilisateur(null, "utilisateur", encoder.encode("user1234"),   roleUser));
        utilisateurRepo.save(new Utilisateur(null, "mbarhoumi",   encoder.encode("pass1234"),   roleUser));
        utilisateurRepo.save(new Utilisateur(null, "sbenali",     encoder.encode("pass1234"),   roleResp));

        // 3. DOMAINES (inchangé)
        Domaine dInfo   = domaineRepo.save(new Domaine(null, "Informatique & Numérique"));
        Domaine dFin    = domaineRepo.save(new Domaine(null, "Finance & Comptabilité"));
        Domaine dMgt    = domaineRepo.save(new Domaine(null, "Management & Leadership"));
        Domaine dRH     = domaineRepo.save(new Domaine(null, "Ressources Humaines"));
        Domaine dMec    = domaineRepo.save(new Domaine(null, "Mécanique & Maintenance"));
        Domaine dJur    = domaineRepo.save(new Domaine(null, "Juridique & Conformité"));
        Domaine dQSE    = domaineRepo.save(new Domaine(null, "Qualité, Sécurité & Environnement"));
        Domaine dLng    = domaineRepo.save(new Domaine(null, "Langues & Communication"));

        // 4. PROFILS
        Profil pInfo5   = profilRepo.save(new Profil(null, "Informaticien Bac+5"));
        Profil pInfo3   = profilRepo.save(new Profil(null, "Informaticien Bac+3"));
        Profil pGest    = profilRepo.save(new Profil(null, "Gestionnaire"));
        Profil pJur     = profilRepo.save(new Profil(null, "Juriste"));
        Profil pTech    = profilRepo.save(new Profil(null, "Technicien Supérieur"));
        Profil pCompt   = profilRepo.save(new Profil(null, "Comptable"));
        Profil pIngMec  = profilRepo.save(new Profil(null, "Ingénieur Mécanique"));
        Profil pDRH     = profilRepo.save(new Profil(null, "Responsable RH"));

        // 5. STRUCTURES
        Structure sDC   = structureRepo.save(new Structure(null, "Direction Centrale"));
        Structure sDRTunis  = structureRepo.save(new Structure(null, "Direction Régionale Tunis"));
        Structure sDRSfax   = structureRepo.save(new Structure(null, "Direction Régionale Sfax"));
        Structure sDRSousse = structureRepo.save(new Structure(null, "Direction Régionale Sousse"));
        Structure sDRBizerte= structureRepo.save(new Structure(null, "Direction Régionale Bizerte"));
        Structure sDSI      = structureRepo.save(new Structure(null, "Direction des Systèmes d'Information"));
        Structure sDRH      = structureRepo.save(new Structure(null, "Direction des Ressources Humaines"));
        Structure sDF       = structureRepo.save(new Structure(null, "Direction Financière"));

        // 6. EMPLOYEURS
        Employeur eGB    = employeurRepo.save(new Employeur(null, "Green Building"));
        Employeur eMSFT  = employeurRepo.save(new Employeur(null, "Microsoft Tunisie"));
        Employeur eOracle= employeurRepo.save(new Employeur(null, "Oracle Consulting"));
        Employeur eUTM   = employeurRepo.save(new Employeur(null, "Université de Tunis El Manar"));
        Employeur eISI   = employeurRepo.save(new Employeur(null, "ISI Tunis"));
        Employeur eCabinet= employeurRepo.save(new Employeur(null, "Cabinet Conseil RH Plus"));
        Employeur eISOCert= employeurRepo.save(new Employeur(null, "ISO Cert Maghreb"));

        // 7. FORMATEURS (inchangé)
        Formateur f1 = formateurRepo.save(new Formateur(null, "Ben Salah",  "Karim",   "k.bensalah@greenbuilding.tn",  "+216 71 234 567", "INTERNE", eGB));
        Formateur f2 = formateurRepo.save(new Formateur(null, "Trabelsi",   "Sonia",   "s.trabelsi@greenbuilding.tn",  "+216 71 234 568", "INTERNE", eGB));
        Formateur f3 = formateurRepo.save(new Formateur(null, "Gharbi",     "Mehdi",   "m.gharbi@microsoft.tn",        "+216 71 345 678", "EXTERNE", eMSFT));
        Formateur f4 = formateurRepo.save(new Formateur(null, "Ammar",      "Youssef", "y.ammar@oracle.com",           "+216 71 456 789", "EXTERNE", eOracle));
        Formateur f5 = formateurRepo.save(new Formateur(null, "Ferchichi",  "Nadia",   "n.ferchichi@utm.tn",           "+216 71 567 890", "EXTERNE", eUTM));
        Formateur f6 = formateurRepo.save(new Formateur(null, "Hamdi",      "Slim",    "s.hamdi@isi.rnu.tn",           "+216 71 678 901", "EXTERNE", eISI));
        Formateur f7 = formateurRepo.save(new Formateur(null, "Zouari",     "Fatma",   "f.zouari@cabinetRH.tn",        "+216 71 789 012", "EXTERNE", eCabinet));
        Formateur f8 = formateurRepo.save(new Formateur(null, "Mejri",      "Hassen",  "h.mejri@isocert.tn",           "+216 71 890 123", "EXTERNE", eISOCert));
        Formateur f9 = formateurRepo.save(new Formateur(null, "Chaabane",   "Ines",    "i.chaabane@greenbuilding.tn",  "+216 71 234 999", "INTERNE", eGB));
        Formateur f10= formateurRepo.save(new Formateur(null, "Nasri",      "Bilel",   "b.nasri@greenbuilding.tn",     "+216 71 234 888", "INTERNE", eGB));

        // 8. FORMATIONS (inchangé, List.of() est ok)
        Formation fm1  = formationRepo.save(new Formation(null, "Développement Java EE Avancé",                2022, 5,  4500.0,  dInfo, f6,  List.of()));
        Formation fm2  = formationRepo.save(new Formation(null, "Gestion Financière et Budgétaire",            2022, 3,  3200.0,  dFin,  f2,  List.of()));
        Formation fm3  = formationRepo.save(new Formation(null, "Leadership & Management d'équipe",            2022, 2,  2800.0,  dMgt,  f7,  List.of()));
        Formation fm4  = formationRepo.save(new Formation(null, "Maintenance Industrielle Préventive",         2022, 4,  3800.0,  dMec,  f1,  List.of()));
        Formation fm5  = formationRepo.save(new Formation(null, "Cybersécurité et Protection des données",     2023, 5,  5200.0,  dInfo, f3,  List.of()));
        Formation fm6  = formationRepo.save(new Formation(null, "Comptabilité IFRS",                           2023, 3,  3500.0,  dFin,  f2,  List.of()));
        Formation fm7  = formationRepo.save(new Formation(null, "Droit du Travail Tunisien",                   2023, 2,  2500.0,  dJur,  f5,  List.of()));
        Formation fm8  = formationRepo.save(new Formation(null, "Certification ISO 9001:2015",                 2023, 4,  4200.0,  dQSE,  f8,  List.of()));
        Formation fm9  = formationRepo.save(new Formation(null, "Gestion des Ressources Humaines",             2023, 3,  3100.0,  dRH,   f7,  List.of()));
        Formation fm10 = formationRepo.save(new Formation(null, "Spring Boot & Microservices",                 2024, 5,  5500.0,  dInfo, f6,  List.of()));
        Formation fm11 = formationRepo.save(new Formation(null, "Power BI & Tableaux de Bord",                 2024, 3,  3800.0,  dInfo, f3,  List.of()));
        Formation fm12 = formationRepo.save(new Formation(null, "Contrôle de Gestion Avancé",                  2024, 4,  4100.0,  dFin,  f2,  List.of()));
        Formation fm13 = formationRepo.save(new Formation(null, "Communication & Prise de parole",             2024, 2,  2200.0,  dLng,  f9,  List.of()));
        Formation fm14 = formationRepo.save(new Formation(null, "Sécurité au Travail & Gestes d'urgence",      2024, 2,  1800.0,  dQSE,  f8,  List.of()));
        Formation fm15 = formationRepo.save(new Formation(null, "Gestion de Projet Agile / Scrum",             2024, 3,  3600.0,  dMgt,  f4,  List.of()));
        Formation fm16 = formationRepo.save(new Formation(null, "Intelligence Artificielle & Machine Learning", 2025, 5, 6000.0,  dInfo, f3,  List.of()));
        Formation fm17 = formationRepo.save(new Formation(null, "Angular & TypeScript Moderne",                2025, 4,  4700.0,  dInfo, f6,  List.of()));
        Formation fm18 = formationRepo.save(new Formation(null, "Audit Interne ISO 19011",                     2025, 3,  3900.0,  dQSE,  f8,  List.of()));
        Formation fm19 = formationRepo.save(new Formation(null, "Anglais Professionnel B2",                    2025, 10, 2800.0,  dLng,  f9,  List.of()));
        Formation fm20 = formationRepo.save(new Formation(null, "Cloud Computing & Azure",                     2025, 5,  5800.0,  dInfo, f3,  List.of()));
        Formation fm21 = formationRepo.save(new Formation(null, "Recrutement & Marque Employeur",              2025, 2,  2400.0,  dRH,   f7,  List.of()));
        Formation fm22 = formationRepo.save(new Formation(null, "DevOps & CI/CD avec Docker",                  2026, 5,  5900.0,  dInfo, f4,  List.of()));
        Formation fm23 = formationRepo.save(new Formation(null, "Transformation Digitale",                     2026, 3,  4300.0,  dMgt,  f10, List.of()));
        Formation fm24 = formationRepo.save(new Formation(null, "Fiscalité des Entreprises 2026",              2026, 3,  3700.0,  dFin,  f5,  List.of()));

        // 9. PARTICIPANTS – ICI : utilisation de new ArrayList<>() au lieu de HashSet
        List<Participant> participants = List.of(
                new Participant(null,"Ben Amor",  "Ahmed",   "a.benamor@greenbuilding.tn",  "+216 55 100 001", sDC,       pInfo5, new ArrayList<>()),
                new Participant(null,"Trabelsi",  "Mariem",  "m.trabelsi@greenbuilding.tn", "+216 55 100 002", sDRTunis,  pGest,  new ArrayList<>()),
                new Participant(null,"Saadi",     "Khaled",  "k.saadi@greenbuilding.tn",    "+216 55 100 003", sDRSfax,   pTech,  new ArrayList<>()),
                new Participant(null,"Mansouri",  "Ines",    "i.mansouri@greenbuilding.tn", "+216 55 100 004", sDC,       pCompt, new ArrayList<>()),
                new Participant(null,"Bouzid",    "Tarek",   "t.bouzid@greenbuilding.tn",   "+216 55 100 005", sDSI,      pInfo3, new ArrayList<>()),
                new Participant(null,"Oueslati",  "Sarra",   "s.oueslati@greenbuilding.tn", "+216 55 100 006", sDRH,      pDRH,   new ArrayList<>()),
                new Participant(null,"Hamrouni",  "Mohamed", "m.hamrouni@greenbuilding.tn", "+216 55 100 007", sDRSousse, pIngMec,new ArrayList<>()),
                new Participant(null,"Khelifi",   "Rania",   "r.khelifi@greenbuilding.tn",  "+216 55 100 008", sDF,       pCompt, new ArrayList<>()),
                new Participant(null,"Mbarki",    "Oussama", "o.mbarki@greenbuilding.tn",   "+216 55 100 009", sDC,       pJur,   new ArrayList<>()),
                new Participant(null,"Ayadi",     "Nesrine", "n.ayadi@greenbuilding.tn",    "+216 55 100 010", sDRBizerte,pGest,  new ArrayList<>()),
                new Participant(null,"Chakroun",  "Farouk",  "f.chakroun@greenbuilding.tn", "+216 55 100 011", sDSI,      pInfo5, new ArrayList<>()),
                new Participant(null,"Zouari",    "Leila",   "l.zouari@greenbuilding.tn",   "+216 55 100 012", sDRH,      pDRH,   new ArrayList<>()),
                new Participant(null,"Tlili",     "Anis",    "a.tlili@greenbuilding.tn",    "+216 55 100 013", sDRSfax,   pTech,  new ArrayList<>()),
                new Participant(null,"Boussetta", "Hajer",   "h.boussetta@greenbuilding.tn","+216 55 100 014", sDF,       pGest,  new ArrayList<>()),
                new Participant(null,"Ferchichi", "Walid",   "w.ferchichi@greenbuilding.tn","+216 55 100 015", sDC,       pInfo5, new ArrayList<>()),
                new Participant(null,"Ghariani",  "Soumaya", "s.ghariani@greenbuilding.tn", "+216 55 100 016", sDRTunis,  pCompt, new ArrayList<>()),
                new Participant(null,"Mejri",     "Yassine", "y.mejri@greenbuilding.tn",    "+216 55 100 017", sDSI,      pInfo3, new ArrayList<>()),
                new Participant(null,"Nasri",     "Donia",   "d.nasri@greenbuilding.tn",    "+216 55 100 018", sDRSousse, pJur,   new ArrayList<>()),
                new Participant(null,"Rezgui",    "Bassem",  "b.rezgui@greenbuilding.tn",   "+216 55 100 019", sDRBizerte,pIngMec,new ArrayList<>()),
                new Participant(null,"Laabidi",   "Amira",   "a.laabidi@greenbuilding.tn",  "+216 55 100 020", sDC,       pDRH,   new ArrayList<>())
        );
        List<Participant> saved = participantRepo.saveAll(participants);

        // 10. INSCRIPTIONS (méthode modifiée pour utiliser List)
        inscrire(saved.get(0),  List.of(fm1,  fm5,  fm10, fm16));
        inscrire(saved.get(1),  List.of(fm2,  fm6,  fm12));
        inscrire(saved.get(2),  List.of(fm4,  fm8,  fm14));
        inscrire(saved.get(3),  List.of(fm3,  fm7,  fm9));
        inscrire(saved.get(4),  List.of(fm1,  fm10, fm11, fm22));
        inscrire(saved.get(5),  List.of(fm5,  fm15, fm21));
        inscrire(saved.get(6),  List.of(fm4,  fm12, fm23));
        inscrire(saved.get(7),  List.of(fm6,  fm13));
        inscrire(saved.get(8),  List.of(fm7,  fm18, fm20));
        inscrire(saved.get(9),  List.of(fm2,  fm9,  fm19));
        inscrire(saved.get(10), List.of(fm1, fm3, fm5));
        inscrire(saved.get(11), List.of(fm8, fm14, fm21));
        inscrire(saved.get(12), List.of(fm11, fm17, fm22));
        inscrire(saved.get(13), List.of(fm12, fm15, fm23));
        inscrire(saved.get(14), List.of(fm16, fm20, fm24));
        inscrire(saved.get(15), List.of(fm1, fm6, fm9));
        inscrire(saved.get(16), List.of(fm10, fm17, fm22));
        inscrire(saved.get(17), List.of(fm13, fm19, fm21));
        inscrire(saved.get(18), List.of(fm7, fm12, fm18));
        inscrire(saved.get(19), List.of(fm4, fm14, fm23));

        System.out.println("🎉 Initialisation terminée !");
    }

    // Méthode utilitaire adaptée à List (pas de gestion de doublon, juste limite 4)
    private void inscrire(Participant p, List<Formation> formations) {
        List<Formation> participantFormations = p.getFormations();
        for (Formation f : formations) {
            if (participantFormations.size() < 4) {
                participantFormations.add(f);
            }
        }
        participantRepo.save(p);
    }
}