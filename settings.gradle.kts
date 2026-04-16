rootProject.name = "api"

include(":bootstrap")
include(":shared:kernel")
include(":shared:infrastructure")

// question
include(":question:domain")
include(":question:port")
include(":question:application")
include(":question:adapter:web")
include(":question:adapter:persistence")
