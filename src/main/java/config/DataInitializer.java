package com.isi.gestion_formation.config;

import com.isi.gestion_formation.entity.*;
import com.isi.gestion_formation.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

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
        // Vérifier si la base est déjà remplie (par exemple via les utilisateurs)
        if (utilisateurRepo.count() > 0) {
            System.out.println("✅ Base déjà initialisée — DataInitializer ignoré.");
            return;
        }

        System.out.println("🚀 Initialisation massive des données de démonstration...");

        // 1. RÔLES
        Role roleAdmin  = roleRepo.save(new Role(null, "ADMIN"));
        Role roleResp   = roleRepo.save(new Role(null, "RESPONSABLE"));
        Role roleUser   = roleRepo.save(new Role(null, "UTILISATEUR"));

        // 2. UTILISATEURS
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        utilisateurRepo.save(new Utilisateur(
                null,
                "admin",
                "admin@greenbuilding.tn",
                encoder.encode("GreenB@Admin2026#"),
                roleAdmin
        ));

        utilisateurRepo.save(new Utilisateur(
                null,
                "responsable",
                "responsable@greenbuilding.tn",
                encoder.encode("GreenB@Resp2026#"),
                roleResp
        ));

        utilisateurRepo.save(new Utilisateur(
                null,
                "utilisateur",
                "utilisateur@greenbuilding.tn",
                encoder.encode("GreenB@User2026#"),
                roleUser
        ));

        // 3. DOMAINES
        Domaine dInfo   = domaineRepo.save(new Domaine(null, "Informatique & Numérique"));
        Domaine dFin    = domaineRepo.save(new Domaine(null, "Finance & Comptabilité"));
        Domaine dMgt    = domaineRepo.save(new Domaine(null, "Management & Leadership"));
        Domaine dRH     = domaineRepo.save(new Domaine(null, "Ressources Humaines"));
        Domaine dMec    = domaineRepo.save(new Domaine(null, "Mécanique & Maintenance"));
        Domaine dJur    = domaineRepo.save(new Domaine(null, "Juridique & Conformité"));
        Domaine dQSE    = domaineRepo.save(new Domaine(null, "Qualité, Sécurité & Environnement"));
        Domaine dLng    = domaineRepo.save(new Domaine(null, "Langues & Communication"));
        List<Domaine> domaines = List.of(dInfo, dFin, dMgt, dRH, dMec, dJur, dQSE, dLng);

        // 4. PROFILS
        Profil pInfo5   = profilRepo.save(new Profil(null, "Informaticien Bac+5"));
        Profil pInfo3   = profilRepo.save(new Profil(null, "Informaticien Bac+3"));
        Profil pGest    = profilRepo.save(new Profil(null, "Gestionnaire"));
        Profil pJur     = profilRepo.save(new Profil(null, "Juriste"));
        Profil pTech    = profilRepo.save(new Profil(null, "Technicien Supérieur"));
        Profil pCompt   = profilRepo.save(new Profil(null, "Comptable"));
        Profil pIngMec  = profilRepo.save(new Profil(null, "Ingénieur Mécanique"));
        Profil pDRH     = profilRepo.save(new Profil(null, "Responsable RH"));
        List<Profil> profils = List.of(pInfo5, pInfo3, pGest, pJur, pTech, pCompt, pIngMec, pDRH);

        // 5. STRUCTURES
        Structure sDC       = structureRepo.save(new Structure(null, "Direction Centrale"));
        Structure sDRTunis  = structureRepo.save(new Structure(null, "Direction Régionale Tunis"));
        Structure sDRSfax   = structureRepo.save(new Structure(null, "Direction Régionale Sfax"));
        Structure sDRSousse = structureRepo.save(new Structure(null, "Direction Régionale Sousse"));
        Structure sDRBizerte= structureRepo.save(new Structure(null, "Direction Régionale Bizerte"));
        Structure sDSI      = structureRepo.save(new Structure(null, "Direction des Systèmes d'Information"));
        Structure sDRH      = structureRepo.save(new Structure(null, "Direction des Ressources Humaines"));
        Structure sDF       = structureRepo.save(new Structure(null, "Direction Financière"));
        List<Structure> structures = List.of(sDC, sDRTunis, sDRSfax, sDRSousse, sDRBizerte, sDSI, sDRH, sDF);

        // 6. EMPLOYEURS
        Employeur eGB       = employeurRepo.save(new Employeur(null, "Green Building"));
        Employeur eMSFT     = employeurRepo.save(new Employeur(null, "Microsoft Tunisie"));
        Employeur eOracle   = employeurRepo.save(new Employeur(null, "Oracle Consulting"));
        Employeur eUTM      = employeurRepo.save(new Employeur(null, "Université de Tunis El Manar"));
        Employeur eISI      = employeurRepo.save(new Employeur(null, "ISI Tunis"));
        Employeur eCabinet  = employeurRepo.save(new Employeur(null, "Cabinet Conseil RH Plus"));
        Employeur eISOCert  = employeurRepo.save(new Employeur(null, "ISO Cert Maghreb"));
        List<Employeur> employeurs = List.of(eGB, eMSFT, eOracle, eUTM, eISI, eCabinet, eISOCert);

        // 7. FORMATEURS (10 existants + 20 supplémentaires)
        List<Formateur> formateurs = new ArrayList<>();
        // 10 formateurs de base
        formateurs.add(formateurRepo.save(new Formateur(null, "Ben Salah",  "Karim",   "k.bensalah@greenbuilding.tn",  "+216 71 234 567", "INTERNE", eGB)));
        formateurs.add(formateurRepo.save(new Formateur(null, "Trabelsi",   "Sonia",   "s.trabelsi@greenbuilding.tn",  "+216 71 234 568", "INTERNE", eGB)));
        formateurs.add(formateurRepo.save(new Formateur(null, "Gharbi",     "Mehdi",   "m.gharbi@microsoft.tn",        "+216 71 345 678", "EXTERNE", eMSFT)));
        formateurs.add(formateurRepo.save(new Formateur(null, "Ammar",      "Youssef", "y.ammar@oracle.com",           "+216 71 456 789", "EXTERNE", eOracle)));
        formateurs.add(formateurRepo.save(new Formateur(null, "Ferchichi",  "Nadia",   "n.ferchichi@utm.tn",           "+216 71 567 890", "EXTERNE", eUTM)));
        formateurs.add(formateurRepo.save(new Formateur(null, "Hamdi",      "Slim",    "s.hamdi@isi.rnu.tn",           "+216 71 678 901", "EXTERNE", eISI)));
        formateurs.add(formateurRepo.save(new Formateur(null, "Zouari",     "Fatma",   "f.zouari@cabinetRH.tn",        "+216 71 789 012", "EXTERNE", eCabinet)));
        formateurs.add(formateurRepo.save(new Formateur(null, "Mejri",      "Hassen",  "h.mejri@isocert.tn",           "+216 71 890 123", "EXTERNE", eISOCert)));
        formateurs.add(formateurRepo.save(new Formateur(null, "Chaabane",   "Ines",    "i.chaabane@greenbuilding.tn",  "+216 71 234 999", "INTERNE", eGB)));
        formateurs.add(formateurRepo.save(new Formateur(null, "Nasri",      "Bilel",   "b.nasri@greenbuilding.tn",     "+216 71 234 888", "INTERNE", eGB)));

        // Ajout de 20 formateurs
        String[] nomsFormateurs = {"Martin", "Durand", "Lefevre", "Moreau", "Simon", "Laurent", "Michel", "Garcia", "David", "Bertrand",
                "Roux", "Vincent", "Fournier", "Morel", "Girard", "Andre", "Leroy", "Mercier", "Blanc", "Garnier"};
        String[] prenomsFormateurs = {"Jean", "Pierre", "Paul", "Jacques", "André", "Louis", "Charles", "Robert", "Michel", "Henri",
                "François", "Bernard", "Philippe", "Nicolas", "Sébastien", "Thierry", "Patrick", "Christophe", "Alexandre", "Julien"};
        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
            String nom = nomsFormateurs[i % nomsFormateurs.length] + (i / nomsFormateurs.length);
            String prenom = prenomsFormateurs[i % prenomsFormateurs.length];
            Employeur emp = employeurs.get(rand.nextInt(employeurs.size()));
            String type = rand.nextBoolean() ? "INTERNE" : "EXTERNE";
            Formateur f = new Formateur(null, nom, prenom, nom.toLowerCase() + "." + prenom.toLowerCase() + i + "@formateur.tn",
                    "99" + String.format("%06d", i), type, emp);
            formateurs.add(formateurRepo.save(f));
        }
        System.out.println("✅ " + formateurs.size() + " formateurs créés.");

        // 8. FORMATIONS : 24 existantes + 50 supplémentaires
        List<Formation> formations = new ArrayList<>();
        // Ajout des 24 formations originales (vous pouvez garder les noms existants)
        formations.addAll(List.of(
                formationRepo.save(new Formation(null, "Développement Java EE Avancé",                2022, 5,  4500.0,  dInfo, formateurs.get(5),  List.of())),
                formationRepo.save(new Formation(null, "Gestion Financière et Budgétaire",            2022, 3,  3200.0,  dFin,  formateurs.get(1),  List.of())),
                formationRepo.save(new Formation(null, "Leadership & Management d'équipe",            2022, 2,  2800.0,  dMgt,  formateurs.get(6),  List.of())),
                formationRepo.save(new Formation(null, "Maintenance Industrielle Préventive",         2022, 4,  3800.0,  dMec,  formateurs.get(0),  List.of())),
                formationRepo.save(new Formation(null, "Cybersécurité et Protection des données",     2023, 5,  5200.0,  dInfo, formateurs.get(2),  List.of())),
                formationRepo.save(new Formation(null, "Comptabilité IFRS",                           2023, 3,  3500.0,  dFin,  formateurs.get(1),  List.of())),
                formationRepo.save(new Formation(null, "Droit du Travail Tunisien",                   2023, 2,  2500.0,  dJur,  formateurs.get(4),  List.of())),
                formationRepo.save(new Formation(null, "Certification ISO 9001:2015",                 2023, 4,  4200.0,  dQSE,  formateurs.get(7),  List.of())),
                formationRepo.save(new Formation(null, "Gestion des Ressources Humaines",             2023, 3,  3100.0,  dRH,   formateurs.get(6),  List.of())),
                formationRepo.save(new Formation(null, "Spring Boot & Microservices",                 2024, 5,  5500.0,  dInfo, formateurs.get(5),  List.of())),
                formationRepo.save(new Formation(null, "Power BI & Tableaux de Bord",                 2024, 3,  3800.0,  dInfo, formateurs.get(2),  List.of())),
                formationRepo.save(new Formation(null, "Contrôle de Gestion Avancé",                  2024, 4,  4100.0,  dFin,  formateurs.get(1),  List.of())),
                formationRepo.save(new Formation(null, "Communication & Prise de parole",             2024, 2,  2200.0,  dLng,  formateurs.get(8),  List.of())),
                formationRepo.save(new Formation(null, "Sécurité au Travail & Gestes d'urgence",      2024, 2,  1800.0,  dQSE,  formateurs.get(7),  List.of())),
                formationRepo.save(new Formation(null, "Gestion de Projet Agile / Scrum",             2024, 3,  3600.0,  dMgt,  formateurs.get(3),  List.of())),
                formationRepo.save(new Formation(null, "Intelligence Artificielle & Machine Learning", 2025, 5, 6000.0,  dInfo, formateurs.get(2),  List.of())),
                formationRepo.save(new Formation(null, "Angular & TypeScript Moderne",                2025, 4,  4700.0,  dInfo, formateurs.get(5),  List.of())),
                formationRepo.save(new Formation(null, "Audit Interne ISO 19011",                     2025, 3,  3900.0,  dQSE,  formateurs.get(7),  List.of())),
                formationRepo.save(new Formation(null, "Anglais Professionnel B2",                    2025, 10, 2800.0,  dLng,  formateurs.get(8),  List.of())),
                formationRepo.save(new Formation(null, "Cloud Computing & Azure",                     2025, 5,  5800.0,  dInfo, formateurs.get(2),  List.of())),
                formationRepo.save(new Formation(null, "Recrutement & Marque Employeur",              2025, 2,  2400.0,  dRH,   formateurs.get(6),  List.of())),
                formationRepo.save(new Formation(null, "DevOps & CI/CD avec Docker",                  2026, 5,  5900.0,  dInfo, formateurs.get(3),  List.of())),
                formationRepo.save(new Formation(null, "Transformation Digitale",                     2026, 3,  4300.0,  dMgt,  formateurs.get(9),  List.of())),
                formationRepo.save(new Formation(null, "Fiscalité des Entreprises 2026",              2026, 3,  3700.0,  dFin,  formateurs.get(4),  List.of()))
        ));

        // Génération de 50 formations supplémentaires (10 par année 2022-2026)
        String[] titresTech = {"Python Avancé", "React Moderne", "Docker & Kubernetes", "Node.js", "GraphQL",
                "Cybersécurité Avancée", "Blockchain", "Big Data", "Machine Learning", "Data Engineering"};
        String[] titresGestion = {"Stratégie d'entreprise", "Négociation commerciale", "Gestion du stress", "Team building",
                "Comptabilité approfondie", "Analyse financière", "Gestion de la paie", "Droit des sociétés",
                "Marketing digital", "Communication interne"};
        String[] tousTitres = new String[titresTech.length + titresGestion.length];
        System.arraycopy(titresTech, 0, tousTitres, 0, titresTech.length);
        System.arraycopy(titresGestion, 0, tousTitres, titresTech.length, titresGestion.length);

        for (int annee = 2022; annee <= 2026; annee++) {
            for (int i = 0; i < 10; i++) {
                String titre = tousTitres[rand.nextInt(tousTitres.length)] + " " + annee + " (" + (i+1) + ")";
                Domaine domaine = domaines.get(rand.nextInt(domaines.size()));
                Formateur formateur = formateurs.get(rand.nextInt(formateurs.size()));
                int duree = 2 + rand.nextInt(8);
                double budget = 1000 + rand.nextDouble() * 9000;
                Formation f = new Formation(null, titre, annee, duree, budget, domaine, formateur, List.of());
                formations.add(formationRepo.save(f));
            }
        }
        System.out.println("✅ " + formations.size() + " formations créées.");

        // 9. PARTICIPANTS : 20 existants + 180 supplémentaires (total 200)
        List<Participant> participants = new ArrayList<>();
        // Participants existants (20)
        participants.addAll(List.of(
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
        ));
        // Création de 180 participants supplémentaires avec emails sans espaces
        String[] nomsPart = {"Ben Ali", "Trabelsi", "Gharbi", "Mansouri", "Saadi", "Bouzid", "Hamrouni", "Khelifi", "Mbarki", "Ayadi",
                "Chakroun", "Zouari", "Tlili", "Boussetta", "Ferchichi", "Ghariani", "Mejri", "Nasri", "Rezgui", "Laabidi"};
        String[] prenomsPart = {"Ahmed", "Sonia", "Mehdi", "Ines", "Khaled", "Mariem", "Youssef", "Rania", "Oussama", "Nesrine",
                "Farouk", "Leila", "Anis", "Hajer", "Walid", "Soumaya", "Yassine", "Donia", "Bassem", "Amira"};
        for (int i = 0; i < 180; i++) {
            String nom = nomsPart[i % nomsPart.length] + (i / nomsPart.length);
            String prenom = prenomsPart[i % prenomsPart.length];
            // Nettoyer les espaces pour former un email valide
            String nomClean = nom.replaceAll("\\s+", "").toLowerCase();
            String prenomClean = prenom.replaceAll("\\s+", "").toLowerCase();
            String email = nomClean + "." + prenomClean + i + "@greenbuilding.tn";
            String tel = "55" + String.format("%06d", i);
            Structure structure = structures.get(rand.nextInt(structures.size()));
            Profil profil = profils.get(rand.nextInt(profils.size()));
            Participant p = new Participant(null, nom, prenom, email, tel, structure, profil, new ArrayList<>());
            participants.add(participantRepo.save(p));
        }
        System.out.println("✅ " + participants.size() + " participants créés.");

        // 10. INSCRIPTIONS : chaque participant s'inscrit à 1 à 4 formations aléatoires
        List<Formation> allFormations = formationRepo.findAll();
        for (Participant p : participants) {
            Collections.shuffle(allFormations);
            int nb = 1 + rand.nextInt(4); // 1 à 4 formations
            List<Formation> selected = new ArrayList<>();
            for (int i = 0; i < nb && i < allFormations.size(); i++) {
                selected.add(allFormations.get(i));
            }
            inscrire(p, selected);
        }

        System.out.println("🎉 Initialisation terminée ! " + participants.size() + " participants, "
                + formations.size() + " formations, " + formateurs.size() + " formateurs.");
    }

    private void inscrire(Participant p, List<Formation> formations) {
        List<Formation> participantFormations = p.getFormations();
        for (Formation f : formations) {
            if (participantFormations.size() < 4 && !participantFormations.contains(f)) {
                participantFormations.add(f);
            }
        }
        participantRepo.save(p);
    }
}