Software Requirements Specification (SRS) for a Mock Evaluation Process
1. Introduction
1.1 Purpose
This document outlines the Software Requirements Specification (SRS) for a system designed to manage a mock evaluation process. The system will automate the scheduling, tracking, and evaluation of participants across multiple batches, technologies, and rounds. The primary goal is to provide a flexible and configurable platform for conducting and analyzing mock evaluations efficiently.
1.2 Scope
The system will manage the entire mock evaluation lifecycle, including:
●	User Management: Roles for administrators and evaluators.
●	Configuration: The ability to configure batches, technologies, and the number of evaluation rounds.
●	Evaluation Management: The process of assigning participants to evaluators, conducting evaluations, and recording scores.
●	Reporting: Generating reports on evaluation results and progress.
The system will not handle the actual content of the evaluation questions, which will be managed outside the platform.
1.3 Definitions and Acronyms
●	SRS: Software Requirements Specification
●	Admin: Administrator
●	Participant: An individual being evaluated.
●	Evaluator: An individual conducting the evaluation.
●	Batch: A group of participants.
●	Technology: The specific skill set or domain (e.g., Java, Python, Data Science).
●	Round: A stage of the evaluation process.
2. Overall Description
2.1 Product Perspective
The system will be a web-based application accessible through a standard web browser. It will operate as a standalone system with no dependencies on other major software. The primary users are Admins and Evaluators.
2.2 Product Functions
●	Admin Functions:
○	Create, view, update, and delete batches.
○	Create, view, update, and delete technologies.
○	Configure the number of evaluation rounds for a given evaluation type.
○	Add and manage user accounts (Evaluators and Participants).
○	Assign Participants to batches and technologies.
○	Assign Evaluators to specific evaluation rounds.
○	View and generate evaluation reports.
●	Evaluator Functions:
○	View a list of assigned Participants.
○	Access evaluation forms to input scores and feedback.
○	Submit evaluation results.
2.3 User Characteristics
●	Admins: Have technical proficiency and will be responsible for system setup and configuration.
●	Evaluators: Possess domain knowledge in the technologies they are evaluating. They should be comfortable using a basic web interface.
●	Participants: While they may not directly interact with the system, their data will be managed within it.
3. System Requirements
3.1 Functional Requirements
3.1.1 Configuration Module
●	FR-1.1: The system shall allow an Admin to create a new batch with a unique name and a start/end date.
●	FR-1.2: The system shall allow an Admin to create a new technology with a unique name.
●	FR-1.3: The system shall allow an Admin to configure the number of rounds for a specific evaluation type (e.g., Java Batch 1 Round 1, Round 2, etc.).
3.1.2 User Management Module
●	FR-2.1: The system shall allow an Admin to add new user accounts, specifying the role (Admin or Evaluator).
●	FR-2.2: The system shall store user details including name, email, and password.
3.1.3 Evaluation Management Module
●	FR-3.1: The system shall allow an Admin to assign Participants to a specific batch and technology.
●	FR-3.2: The system shall allow an Admin to assign Evaluators to specific Participants for a particular round and technology.
●	FR-3.3: The system shall display a list of assigned evaluations to the Evaluator.
●	FR-3.4: The system shall provide an evaluation form where an Evaluator can input scores and comments. The form will dynamically change based on the technology and round.
●	FR-3.5: The system shall record the submitted scores and feedback for each participant and round.
3.1.4 Reporting Module
●	FR-4.1: The system shall generate a report showing the scores for all participants within a specific batch and technology.
●	FR-4.2: The system shall generate a report showing the average score per round for a specific technology.
●	FR-4.3: The system shall allow the Admin to export reports in a common format (e.g., PDF or CSV).
3.2 Non-Functional Requirements
3.2.1 Performance
●	NFR-1.1: The system shall load pages and process requests within 3 seconds under normal load conditions (up to 50 concurrent users).
3.2.2 Security
●	NFR-2.1: All user passwords shall be hashed and salted before being stored in the database.
●	NFR-2.2: The system shall implement a role-based access control (RBAC) system to ensure users can only access authorized functions.
3.2.3 Usability
●	NFR-3.1: The user interface shall be intuitive and easy to navigate for both Admins and Evaluators.
●	NFR-3.2: The system shall provide clear feedback to the user on the status of their actions (e.g., "Evaluation submitted successfully").

