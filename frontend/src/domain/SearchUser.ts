import {UserAvatars} from "@/domain/UserAvatars";
import {UserProfile} from "@/domain/UserProfile";
import {UserLevel} from "@/domain/UserLevel";
import {Profile} from "@/domain/Profile";

export type SearchUser = {
    id: string;
    nickname: string;
    gender: string;
    birthday: Date;
    updateTime: Date;
    experience: number;
    avatars: UserAvatars;
    userProfile: UserProfile;
    level: UserLevel;
    profile: Profile;
}