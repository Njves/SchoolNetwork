package com.njves.schoolnetwork.Models.network.models.profile

import com.njves.schoolnetwork.Models.network.models.auth.Profile
import com.njves.schoolnetwork.Models.network.models.auth.User

data class UserProfile(val user : User,val profile : Profile )