import { lazy } from "react";

export const MyCardsPage = lazy(() =>
	import("./ui/my-cards").then(module => ({ default: module.MyCards }))
);
