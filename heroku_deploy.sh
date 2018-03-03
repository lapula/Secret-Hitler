#!/bin/bash
# Helper for heroku deploy, requires mvn, and heroku logged in
cd frontend/;
npm run build;
cd ..;
mvn clean heroku:deploy;
