-- =====================================================================
-- Données de test pour l'application de suivi de candidatures
-- Base H2 en mémoire : ce script est rejoué à chaque démarrage.
-- Les identifiants sont générés par la base (IDENTITY), dans l'ordre
-- d'insertion : les ids indiqués en commentaire supposent une base vide.
-- =====================================================================

-- ---------------------------------------------------------------------
-- Utilisateurs
-- 1 : utilisateur courant (CurrentUserProvider renvoie 1L)
-- 2 : autre utilisateur, pour tester l'isolation des données (IDOR)
-- Mots de passe en clair provisoirement : ils seront hachés en phase 5.
-- ---------------------------------------------------------------------
INSERT INTO users (name, email, password) VALUES ('Test User', 'test@gmail.com', 'password');
INSERT INTO users (name, email, password) VALUES ('Alice Martin', 'alice.martin@test.com', 'password');

-- ---------------------------------------------------------------------
-- Entreprises
-- 1 à 4 : utilisateur 1 (l'entreprise 4 n'a aucune candidature)
-- 5 et 6 : utilisateur 2
-- ---------------------------------------------------------------------
INSERT INTO enterprise (name, headquarters, user_id) VALUES ('Nantes Digital', 'Nantes', 1);
INSERT INTO enterprise (name, headquarters, user_id) VALUES ('Atlantique Logiciels', 'Saint-Nazaire', 1);
INSERT INTO enterprise (name, headquarters, user_id) VALUES ('Loire Data', 'Angers', 1);
INSERT INTO enterprise (name, headquarters, user_id) VALUES ('Vendée Tech', 'La Roche-sur-Yon', 1);
INSERT INTO enterprise (name, headquarters, user_id) VALUES ('Océan Conseil', 'Brest', 2);
INSERT INTO enterprise (name, headquarters, user_id) VALUES ('Rennes Solutions', 'Rennes', 2);

-- ---------------------------------------------------------------------
-- Candidatures
-- 1 à 7 : utilisateur 1, tous les statuts représentés
-- 8 et 9 : utilisateur 2
-- ---------------------------------------------------------------------
INSERT INTO application (job_title, job_description, monthly_salary, location, offer_link, application_date, notes, status, user_id, enterprise_id)
VALUES ('Développeur Java junior', 'Développement de services REST avec Spring Boot', 2400, 'Nantes',
        'https://jobs.test/nantes-digital/java-junior', '2026-10-05', 'Équipe de 6 développeurs', 'SENT', 1, 1);

INSERT INTO application (job_title, job_description, monthly_salary, location, offer_link, application_date, notes, status, user_id, enterprise_id)
VALUES ('Développeur full-stack Java / Angular', 'Application métier en Spring Boot et Angular', 2700, 'Nantes',
        'https://jobs.test/nantes-digital/fullstack', '2026-10-12', 'Entretien technique prévu le 20/11', 'INTERVIEW', 1, 1);

INSERT INTO application (job_title, job_description, monthly_salary, location, offer_link, application_date, notes, status, user_id, enterprise_id)
VALUES ('Développeur back-end Spring', 'Maintenance d''une API de facturation', 2500, 'Saint-Nazaire',
        'https://jobs.test/atlantique-logiciels/backend', '2026-09-20', 'Profil plus senior recherché', 'REFUSED', 1, 2);

INSERT INTO application (job_title, job_description, monthly_salary, location, offer_link, application_date, notes, status, user_id, enterprise_id)
VALUES ('Ingénieur logiciel Java', 'Refonte d''un outil interne', 2800, 'Saint-Nazaire',
        'https://jobs.test/atlantique-logiciels/ingenieur', '2026-10-01', 'Réponse attendue avant fin novembre', 'OFFER_RECEIVED', 1, 2);

INSERT INTO application (job_title, job_description, monthly_salary, location, offer_link, application_date, notes, status, user_id, enterprise_id)
VALUES ('Développeur Angular', 'Création d''interfaces pour une plateforme de données', NULL, 'Angers',
        'https://jobs.test/loire-data/angular', NULL, 'CV à adapter avant envoi', 'YET_TO_SEND_RESUME', 1, 3);

INSERT INTO application (job_title, job_description, monthly_salary, location, offer_link, application_date, notes, status, user_id, enterprise_id)
VALUES ('Développeur Java', 'Développement de traitements de données', 2600, 'Angers',
        'https://jobs.test/loire-data/java', '2026-11-02', NULL, 'SENT', 1, 3);

INSERT INTO application (job_title, job_description, monthly_salary, location, offer_link, application_date, notes, status, user_id, enterprise_id)
VALUES ('Stage développeur full-stack', NULL, NULL, 'Angers',
        'https://jobs.test/loire-data/stage', '2026-08-15', 'Stage terminé en octobre', 'ACCEPTED', 1, 3);

INSERT INTO application (job_title, job_description, monthly_salary, location, offer_link, application_date, notes, status, user_id, enterprise_id)
VALUES ('Consultant Java', 'Mission chez un client bancaire', 3000, 'Brest',
        'https://jobs.test/ocean-conseil/consultant', '2026-10-20', 'Candidature de l''utilisateur 2', 'INTERVIEW', 2, 5);

INSERT INTO application (job_title, job_description, monthly_salary, location, offer_link, application_date, notes, status, user_id, enterprise_id)
VALUES ('Développeur Spring Boot', 'Microservices pour le secteur public', 2900, 'Rennes',
        'https://jobs.test/rennes-solutions/spring', '2026-11-05', NULL, 'SENT', 2, 6);

-- ---------------------------------------------------------------------
-- Relances
-- 1 et 2 : candidature 1 (utilisateur 1, deux relances)
-- 3      : candidature 2
-- 4      : candidature 4
-- 5      : candidature 6
-- 6      : candidature 8 (utilisateur 2)
-- ---------------------------------------------------------------------
INSERT INTO reminder (reminder_date, commentary, contact_type, application_id) VALUES ('2026-10-15', 'Relance par mail au service RH', 'EMAIL', 1);
INSERT INTO reminder (reminder_date, commentary, contact_type, application_id) VALUES ('2026-10-25', 'Appel à la responsable du recrutement', 'PHONE', 1);
INSERT INTO reminder (reminder_date, commentary, contact_type, application_id) VALUES ('2026-10-22', 'Confirmation de la date d''entretien', 'EMAIL', 2);
INSERT INTO reminder (reminder_date, commentary, contact_type, application_id) VALUES ('2026-11-10', 'Demande de précisions sur le salaire', 'PHONE', 4);
INSERT INTO reminder (reminder_date, commentary, contact_type, application_id) VALUES ('2026-11-12', NULL, 'EMAIL', 6);
INSERT INTO reminder (reminder_date, commentary, contact_type, application_id) VALUES ('2026-10-30', 'Relance de l''utilisateur 2', 'EMAIL', 8);

-- =====================================================================
-- Scénarios de test couverts par ces données (utilisateur courant = 1)
--
-- Cas nominaux
--   GET    /api/enterprises                          → entreprises 1 à 4
--   GET    /api/applications                         → candidatures 1 à 7
--   GET    /api/applications?status=SENT             → candidatures 1 et 6
--   GET    /api/applications/1/reminders             → relances 1 et 2
--   DELETE /api/enterprises/4                        → 204 (aucune candidature)
--   DELETE /api/enterprises/1                        → 204, supprime aussi les candidatures 1 et 2 et leurs relances
--
-- Isolation des données (attendu : 404, jamais les données de l'utilisateur 2)
--   GET    /api/enterprises/5
--   GET    /api/applications/8
--   GET    /api/applications/8/reminders
--   DELETE /api/applications/9
--
-- Sous-ressources (attendu : 404)
--   DELETE /api/applications/2/reminders/1           → la relance 1 appartient à la candidature 1
--   DELETE /api/applications/1/reminders/6           → la relance 6 appartient à l'utilisateur 2
--
-- Références invalides dans le corps (attendu : 422 sur enterpriseId)
--   POST   /api/applications avec "enterpriseId": 5  → entreprise de l'utilisateur 2
--   POST   /api/applications avec "enterpriseId": 999
--
-- Problème N+1 (semaine 4)
--   GET    /api/applications                         → compter les SELECT dans les logs
-- =====================================================================