import { lazy } from "react";

export const SwiperIdPage = lazy(() =>
	import("./ui/swiper-id").then(module => ({ default: module.SwiperId }))
);
