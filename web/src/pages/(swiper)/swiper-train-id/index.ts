import { lazy } from "react";

export const SwiperTrainIdPage = lazy(() =>
	import("./ui/swiper-train-id").then(module => ({
		default: module.SwiperTrainId,
	}))
);
