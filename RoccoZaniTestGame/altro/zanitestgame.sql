-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Mar 14, 2025 alle 23:00
-- Versione del server: 10.4.32-MariaDB
-- Versione PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `zanitestgame`
--

-- --------------------------------------------------------

--
-- Struttura stand-in per le viste `classifica`
-- (Vedi sotto per la vista effettiva)
--
CREATE TABLE `classifica` (
`nome` varchar(20)
,`punteggio` int(8)
);

-- --------------------------------------------------------

--
-- Struttura della tabella `partita`
--

CREATE TABLE `partita` (
  `idPartita` varchar(8) NOT NULL,
  `idUtente` varchar(8) NOT NULL,
  `punteggio` int(8) NOT NULL DEFAULT 0,
  `dataEOraInizio` datetime NOT NULL,
  `dataEOraFine` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `partita`
--

INSERT INTO `partita` (`idPartita`, `idUtente`, `punteggio`, `dataEOraInizio`, `dataEOraFine`) VALUES
('P000001', 'USR001', 1500, '2025-03-10 15:30:00', '2025-03-10 15:45:00'),
('P000002', 'USR002', 1800, '2025-03-12 16:00:00', '2025-03-12 16:20:00'),
('P000003', 'USR001', 2000, '2025-03-13 10:10:00', '2025-03-13 10:30:00'),
('PT34433', 'USR003', 3000, '2025-03-14 18:28:50', '2025-03-14 18:28:54');

-- --------------------------------------------------------

--
-- Struttura della tabella `richiesta`
--

CREATE TABLE `richiesta` (
  `idRichiesta` varchar(8) NOT NULL,
  `email` varchar(100) NOT NULL,
  `idUtente` varchar(8) DEFAULT NULL,
  `messaggio` text NOT NULL,
  `stato` varchar(16) NOT NULL DEFAULT 'creata'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `richiesta`
--

INSERT INTO `richiesta` (`idRichiesta`, `email`, `idUtente`, `messaggio`, `stato`) VALUES
('REQ001', 'mario.rossi@example.com', 'USR001', 'Ho problemi con il login, potete aiutarmi?', 'creata'),
('REQ002', 'luigi.verdi@example.com', 'USR002', 'Come posso recuperare il mio punteggio precedente?', 'creata'),
('REQ003', 'admin@example.com', NULL, 'Vorrei segnalare un bug nel sistema.', 'creata');

-- --------------------------------------------------------

--
-- Struttura della tabella `utente`
--

CREATE TABLE `utente` (
  `idUtente` char(8) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nome` varchar(20) NOT NULL,
  `cognome` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `dataNascita` date NOT NULL,
  `dataRegistrazione` datetime DEFAULT current_timestamp(),
  `fotoProfilo` blob DEFAULT NULL,
  `tipo` varchar(16) NOT NULL,
  `utenteBannato` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `utente`
--

INSERT INTO `utente` (`idUtente`, `username`, `password`, `nome`, `cognome`, `email`, `dataNascita`, `dataRegistrazione`, `fotoProfilo`, `tipo`, `utenteBannato`) VALUES
('USR001', 'player1', 'hashed_password1', 'Mario', 'Rossi', 'mario.rossi@example.com', '1995-06-15', '2025-03-14 18:19:03', NULL, 'utente', 0),
('USR002', 'player2', 'hashed_password2', 'Luigi', 'Verdi', 'luigi.verdi@example.com', '1998-09-23', '2025-03-14 18:19:03', NULL, 'utente', 0),
('USR003', 'admin1', 'hashed_admin_pass', 'Admin', 'Master', 'admin@example.com', '1985-04-10', '2025-03-14 18:19:03', NULL, 'admin', 0);

-- --------------------------------------------------------

--
-- Struttura per vista `classifica`
--
DROP TABLE IF EXISTS `classifica`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `classifica`  AS SELECT `utente`.`nome` AS `nome`, `partita`.`punteggio` AS `punteggio` FROM (`utente` join `partita`) WHERE `utente`.`utenteBannato` = 0 AND `utente`.`idUtente` = `partita`.`idUtente` ORDER BY `partita`.`punteggio` DESC ;

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `partita`
--
ALTER TABLE `partita`
  ADD PRIMARY KEY (`idPartita`),
  ADD KEY `idUtente` (`idUtente`);

--
-- Indici per le tabelle `richiesta`
--
ALTER TABLE `richiesta`
  ADD PRIMARY KEY (`idRichiesta`),
  ADD KEY `idUtente` (`idUtente`);

--
-- Indici per le tabelle `utente`
--
ALTER TABLE `utente`
  ADD PRIMARY KEY (`idUtente`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `partita`
--
ALTER TABLE `partita`
  ADD CONSTRAINT `partita_ibfk_1` FOREIGN KEY (`idUtente`) REFERENCES `utente` (`idUtente`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `richiesta`
--
ALTER TABLE `richiesta`
  ADD CONSTRAINT `richiesta_ibfk_1` FOREIGN KEY (`idUtente`) REFERENCES `utente` (`idUtente`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
