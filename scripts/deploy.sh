#!/bin/bash
export SSHPASS=$DEPLOY_PASS
set -ev
if [[ "${TRAVIS_BRANCH}" = "master" && "${TRAVIS_PULL_REQUEST}" = "false" ]]; then
	docker login -u="${DOCKER_USER}" -p="${DOCKER_PASS}"
	docker build --build-arg tmcappid=${TMC_APP_ID} --build-arg tmcsecret=${TMC_SECRET} -t ${DOCKER_USER}/${DOCKER_REPO} .
	docker push ${DOCKER_USER}/${DOCKER_REPO}
	sshpass -e ssh -p "${DEPLOY_PORT}" "${DEPLOY_USER}"@"${DEPLOY_HOST}" 'deploy.sh'
fi

