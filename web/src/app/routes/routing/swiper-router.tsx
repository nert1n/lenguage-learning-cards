import { SwiperIdPage } from "@pages/(swiper)/swiper-id";
import { SwiperTrainIdPage } from "@pages/(swiper)/swiper-train-id";

export const swiperRoutes = [
	{
		path: "swiper",
		children: [
			{
				path: ":id",
				element: <SwiperIdPage />,
			},
			{
				path: "train",
				children: [
					{
						path: ":id",
						element: <SwiperTrainIdPage />,
					},
				],
			},
		],
	},
];
