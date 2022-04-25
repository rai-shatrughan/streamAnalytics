eval $(minikube docker-env)

mvnw package k8s:build k8s:resource k8s:deploy
