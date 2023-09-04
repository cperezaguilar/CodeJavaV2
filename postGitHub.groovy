def postGitHub(commitId, state, context, description, targetUrl) {
  def payload = JsonOutput.toJson(
    state: state,
    context: context,
    description: description,
    target_url: targetUrl
  )
  sh "curl -H \"Authorization: token ${GITHUBAPITOKEN}\" --request POST --data '${payload}'
  https://api.github.com/repos/${PROJECT}/statuses/${commitId} > /dev/null"
}
